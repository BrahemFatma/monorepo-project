package com.pfe.projet.Reunions;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.pfe.projet.exception.ReunionNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.SimpleDateFormat;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class ReunionService {
    private final EmailService emailService;

    private final ReunionRepository repository;
    private final ReunionMapper mapper;
    private final KafkaProducerService KafkaProducerService;
    private final NotificationService NotificationService;
    private final UserService UserService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    private final String notificationTopic = "notification-topic";
    @Autowired
    public ReunionService(ObjectMapper objectMapper, EmailService emailService, ReunionRepository repository, ReunionMapper mapper,
                          KafkaProducerService kafkaProducerService,
                          com.pfe.projet.Reunions.NotificationService notificationService, com.pfe.projet.Reunions.UserService userService) {
        this.objectMapper = objectMapper;
        this.emailService = emailService;
        this.repository = repository;
        this.mapper = mapper;
        this.KafkaProducerService = kafkaProducerService;
        this.NotificationService = notificationService;
        this.UserService = userService;
    }
    public List<Reunion> findReunionsByTitle(String titre) {
        return repository.findByTitre(titre);
    }

    public List<Reunion> findReunionsByDate(Date date) {
        return repository.findByDate(date);
    }

    public List<Reunion> findReunionsByPlace(String lieu) {
        return repository.findByLieu(lieu);
    }

    public List<Reunion> findReunionsByDescription(String keyword) {
        return repository.findByDescriptionContaining(keyword);
    }
    public List<Reunion> findReunionByUserId(Long userId) {
        // Récupérer toutes les réunions associées à l'ID utilisateur
        return repository.findByUserId(userId);
    }
    public List<String> getParticipantsFullNamesByReunionId(Long reunionId) {
        Reunion reunion = repository.findById(reunionId)
                .orElseThrow(() -> new ReunionNotFoundException("Réunion introuvable avec l'ID : " + reunionId));

        List<Long> userIds = reunion.getUserIds(); // à adapter si le champ a un autre nom

        return userIds.stream()
                .map(userId -> {
                    UserResponse user = UserService.getUserById(userId);
                    return user.getNom() + " " + user.getPrenom();
                })
                .collect(Collectors.toList());
    }

    public Long createReunion(ReunionRequest request) {
        try {
            if (request.userIds() != null && !request.userIds().isEmpty()) {
                for (Long userId : request.userIds()) {
                    List<Reunion> existingReunions = repository.findByUserIdAndDateAndHeure(userId, request.date(), request.heure());
                    if (!existingReunions.isEmpty()) {
                        System.out.println("⚠️ Conflit détecté ! L'utilisateur ID " + userId +
                                " a déjà une réunion à la même date (" + request.date() +
                                ") et heure (" + request.heure() + ").");
                        throw new RuntimeException("Conflit d'horaire pour l'utilisateur ID: " + userId);
                    }
                }
            }

            Reunion reunion = new Reunion(request);
            // Ajout manuel des champs supplémentaires si besoin
            reunion.setType(request.type());
            reunion.setClientId(request.clientId());
            reunion.setDateFin(request.dateFin());
            reunion.setHeureFin(request.heureFin());
            reunion.setDureePrevue(request.dureePrevue());
            reunion.setDureeReelle(request.dureeReelle());
            reunion.setActionMiseEnPlace(request.actionMiseEnPlace());
            reunion.setDateAction(request.dateAction());
            reunion.setResponsableActionId(request.responsableActionId());
            reunion.setClotureAction(request.clotureAction());
            reunion.setDateCloture(request.dateCloture());

            System.out.println("Tentative d'enregistrer la réunion : " + reunion);
            Reunion savedReunion = repository.save(reunion);
            System.out.println("✅ Réunion enregistrée avec succès : " + savedReunion);

            // Formatter la date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(request.date());

            // ✅ ENVOI JSON via KAFKA
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(dateFormat);

            if (request.userIds() != null && !request.userIds().isEmpty()) {
                for (Long userId : request.userIds()) {
                    UserResponse user = UserService.getUserById(userId);

                    ReunionNotificationDTO dto = new ReunionNotificationDTO();
                    dto.setUserId(user.getId());
                    dto.setUserNom(user.getNom());
                    dto.setTitre(savedReunion.getTitre());
                    dto.setLieu(savedReunion.getLieu());
                    dto.setDate(formattedDate);
                    dto.setHeure(savedReunion.getHeure());
                    dto.setDescription(savedReunion.getDescription());
                    dto.setType("CREATION");

                    String jsonMessage = objectMapper.writeValueAsString(dto);
                    kafkaTemplate.send("reunion-notification-topic", jsonMessage);
                }
            } else {
                System.out.println("ℹ️ Aucun utilisateur ciblé pour la réunion. Aucun message Kafka envoyé.");
            }

            return savedReunion.getId();

        } catch (Exception e) {
            System.out.println("❌ Erreur lors de la création de la réunion: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la création de la réunion", e);
        }
    }

    private String getEmailByUserId(Long userId) {
        return "user@example.com";
    }


    public void updateReunion(ReunionRequest request) {
        var reunion = repository.findById(request.id())
                .orElseThrow(() -> new ReunionNotFoundException(
                        format("Cannot update reunion: No reunion found with the provided ID :: %s", request.id())
                ));

        // ✅ État initial de actionMiseEnPlace
        boolean wasActionSet = reunion.getActionMiseEnPlace() != null && reunion.getActionMiseEnPlace();

        mergeReunion(reunion, request);
        Reunion updatedReunion = repository.save(reunion);

        // ✅ Format de la date pour l'affichage
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = request.date() != null ? dateFormat.format(request.date()) : "Date non définie";

        // ✅ Notification générale de mise à jour
        String updateNotification = String.format(
                "Réunion mise à jour : %s, Lieu: %s, Date: %s, Heure: %s, Description: %s",
                request.titre(),
                request.lieu(),
                formattedDate,
                request.heure(),
                request.description()
        );

        // ✅ Notification individuelle aux participants
        if (request.userIds() != null && !request.userIds().isEmpty()) {
            for (Long userId : request.userIds()) {
                try {


                    UserResponse user = UserService.getUserById(userId); // ✅ appel au service utilisateur

                    ReunionNotificationDTO dto = new ReunionNotificationDTO();
                    dto.setUserId(user.getId());
                    dto.setUserNom(user.getNom());
                    dto.setTitre(request.titre());
                    dto.setLieu(request.lieu());
                    dto.setDate(formattedDate);
                    dto.setHeure(request.heure());
                    dto.setDescription(request.description());
                    dto.setType("UPDATE");
                    dto.setDescriptionAction(request.descriptionAction());
                    dto.setDateAction(request.dateAction());

                    String jsonMessage = objectMapper.writeValueAsString(dto);
                    kafkaTemplate.send("reunion-notification-topic", jsonMessage);

                } catch (Exception e) {
                    System.out.println("⚠️ Erreur lors de la récupération du user ID " + userId + " : " + e.getMessage());
                }
            }

        } else {
            kafkaTemplate.send("reunion-notification-topic", updateNotification);
        }

        // ✅ Notification spéciale si actionMiseEnPlace passe de false à true
        boolean isActionNowSet = updatedReunion.getActionMiseEnPlace() != null && updatedReunion.getActionMiseEnPlace();


        // ✅ Notification au responsable de l'action si renseigné (notification générique)
        if (request.responsableActionId() != null) {
            try {
                UserResponse responsable = UserService.getUserById(request.responsableActionId());
                String responsableMessage = String.format(
                        "Mise à jour pour vous, responsable de l'action dans la réunion '%s' prévue le %s : Action prévue le %s, Description de l’action : %s",
                        request.titre(),
                        formattedDate,
                        request.dateAction() != null ? dateFormat.format(request.dateAction()) : "Non définie",
                        request.descriptionAction()
                );

                kafkaTemplate.send("reunion-notification-topic", responsableMessage);
            } catch (Exception e) {
                System.out.println("⚠️ Impossible d’envoyer la notification au responsable : " + e.getMessage());
            }
        }
    }

    private void mergeReunion(Reunion reunion, ReunionRequest request) {
        if (request.date() != null) {
            reunion.setDate(request.date());
        }
        if (request.lieu() != null && !request.lieu().isBlank()) {
            reunion.setLieu(request.lieu());
        }
        if (request.pv() != null && !request.pv().isBlank()) {
            reunion.setPv(request.pv());
        }
        if (request.heure() != null && !request.heure().isBlank()) {
            reunion.setHeure(request.heure());
        }
        if (request.description() != null && !request.description().isBlank()) {
            reunion.setDescription(request.description());
        }
        if (request.titre() != null && !request.titre().isBlank()) {
            reunion.setTitre(request.titre());
        }
        if (request.documents() != null) {
            reunion.setDocuments(request.documents());
        }
        if (request.userIds() != null) {
            reunion.setUserIds(request.userIds());
        }
        if (request.type() != null) {
            reunion.setType(request.type());
        }
        if (request.clientId() != null) {
            reunion.setClientId(request.clientId());
        }
        if (request.dateFin() != null) {
            reunion.setDateFin(request.dateFin());
        }
        if (request.heureFin() != null) {
            reunion.setHeureFin(request.heureFin());
        }
        if (request.dureePrevue() != null) {
            reunion.setDureePrevue(request.dureePrevue());
        }
        if (request.dureeReelle() != null) {
            reunion.setDureeReelle(request.dureeReelle());
        }
        if (request.actionMiseEnPlace() != null) {
            reunion.setActionMiseEnPlace(request.actionMiseEnPlace());
        }
        if (request.dateAction() != null) {
            reunion.setDateAction(request.dateAction());
        }
        if (request.responsableActionId() != null) {
            reunion.setResponsableActionId(request.responsableActionId());
        }
        if (request.clotureAction() != null) {
            reunion.setClotureAction(request.clotureAction());
        }
        if (request.dateCloture() != null) {
            reunion.setDateCloture(request.dateCloture());
        }
        if (request.descriptionAction() != null && !request.descriptionAction().isBlank()) {
            reunion.setDescriptionAction(request.descriptionAction());
        }



    }

    public List<ReunionResponse> findAllReunions() {
        return repository.findAll()
                .stream()
                .map(mapper::fromReunion)
                .collect(Collectors.toList());
    }

    public boolean existisById(Long reunionId) {
        return repository.findById(reunionId).isPresent();
    }

    public ReunionResponse findById(Long reunionId) {
        return repository.findById(reunionId)
                .map(mapper::fromReunion)
                .orElseThrow(() -> new ReunionNotFoundException(
                        format("No reunion found with the provided ID:: %s", reunionId)
                ));
    }

    public void deleteReunion(Long reunionId) {
        repository.deleteById(reunionId);
    }
    public void addDocumentToReunion(Long reunionId, MultipartFile file) throws IOException {
        Reunion reunion = repository.findById(reunionId)
                .orElseThrow(() -> new ReunionNotFoundException("Reunion not found"));

        String encodedFile = Base64.getEncoder().encodeToString(file.getBytes());

        String documents = reunion.getDocuments();
        if (documents == null) {
            reunion.setDocuments(encodedFile);
        } else {
            reunion.setDocuments(documents + ";" + encodedFile);
        }

        repository.save(reunion);
    }

    public byte[] getDocumentPdfByReunionId(Long reunionId) throws IOException {
        Reunion reunion = repository.findById(reunionId).orElse(null);

        if (reunion == null || reunion.getDocuments() == null) {
            return null;
        }
        try {
            return Base64.getDecoder().decode(reunion.getDocuments());
        } catch (IllegalArgumentException e) {
            throw new IOException("Error decoding the document", e);
        }
    }
    public Optional<Reunion> getReunionById(Long id) {
        return repository.findById(id);
    }








}