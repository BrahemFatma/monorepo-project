package com.pfe.projet.notifications;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.SimpleDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Service
public class KafkaConsumerService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "reunion-notification-topic", groupId = "notification-group")
    public void listenToReunionNotification(String message) {
        try {
            System.out.println("📨 Message JSON brut reçu : " + message);

            if (message.trim().startsWith("{")) {
                ObjectMapper objectMapper = new ObjectMapper();
                ReunionNotificationDTO dto = objectMapper.readValue(message, ReunionNotificationDTO.class);

                System.out.println("📊 Données reçues : userId=" + dto.getUserId()
                        + ", titre=" + dto.getTitre()
                        + ", lieu=" + dto.getLieu()
                        + ", date=" + dto.getDate()
                        + ", heure=" + dto.getHeure()
                        + ", description=" + dto.getDescription()
                        + "\n  descriptionAction  = " + dto.getDescriptionAction()
                        + "\n  dateAction         = " + dto.getDateAction());

                if (dto == null || dto.getUserId() == null || dto.getUserNom() == null) {
                    throw new IllegalArgumentException("❌ DTO ou champs obligatoires manquants.");
                }

                System.out.println("✅ Données désérialisées : " + dto.getUserNom() + " | " + dto.getTitre());

                if ("CREATION".equalsIgnoreCase(dto.getType())) {
                    String notificationText = String.format(
                            "Une nouvelle réunion a été créée : %s (ID: %s) : %s, Lieu : %s, Date : %s, Heure : %s, Description : %s",
                            dto.getUserNom(), dto.getUserId(), dto.getTitre(),
                            dto.getLieu(), dto.getDate(), dto.getHeure(), dto.getDescription()
                    );

                    Notification notification = new Notification(dto.getUserId(), notificationText);
                    notificationRepository.save(notification);
                    System.out.println("✅ Notification de création enregistrée pour l'utilisateur ID : " + dto.getUserId());

                    String email = getEmailByUserId(dto.getUserId());
                    if (email != null) {
                        String subject = "Nouvelle réunion créée : " + dto.getTitre();

                        String emailBody = String.format(
                                "Objet : Réunion d’équipe – [%s]\n\n" +
                                        "Chers collègues,\n\n" +
                                        "Je vous informe qu'une réunion aura lieu le %s à %s dans %s pour discuter des points suivants :\n%s\n\n" +
                                        "Merci de confirmer votre présence.\n\n" +
                                        "Cordialement,\nZeyneb Rezgui\nResponsable RH – GPro Consulting",
                                dto.getTitre(), dto.getDate(), dto.getHeure(), dto.getLieu(), dto.getDescription()
                        );

                        emailService.sendEmail(email, subject, emailBody);
                    }

                } else if ("UPDATE".equalsIgnoreCase(dto.getType())) {
                    String notificationText = String.format(
                            "Vous êtes désigné(e) responsable d’une action dans la réunion \"%s\" \n\n" +
                                    "Date de l’action : %s\n" +
                                    "Merci de vous assurer de la bonne réalisation de cette tâche dans les délais.\n\n" ,
                            dto.getTitre(), dto.getDateAction()
                    );

                    Notification notification = new Notification(dto.getUserId(), notificationText);
                    notificationRepository.save(notification);
                    System.out.println("✅ Notification de mise à jour enregistrée pour l'utilisateur ID : " + dto.getUserId());

                }

            } else if (message.contains("responsable de l'action dans la réunion")) {
                System.out.println("🔔 Notification pour le lancement d’une action détectée.");

                String titre = extractBetween(message, "réunion '", "'");
                String dateReunion = extractBetween(message, "prévue le ", " :");
                String dateAction = extractBetween(message, "Action prévue le ", ",");
                String descriptionAction = extractAfter(message, "Description de l’action :");


                String notificationText = String.format(
                        "Vous êtes désigné(e) responsable d’une action dans la réunion \"%s\" prévue le %s.\n\n" +
                                "Date de l’action : %s\n" +
                                "Détails de l’action  : %s\n\n" +
                                "Merci de vous assurer de la bonne réalisation de cette tâche dans les délais.\n\n" +
                                "Cordialement,\nZeyneb Rezgui\nResponsable RH – GPro Consulting",
                        titre, dateReunion, dateAction, descriptionAction
                );

                Long userId = null;
                String userIdStr = extractBetween(message, "Utilisateur ID: ", " ");
                if (userIdStr != null && !userIdStr.isEmpty()) {
                    try {
                        userId = Long.parseLong(userIdStr);
                    } catch (NumberFormatException e) {
                        System.err.println("❌ Impossible de parser l'userId depuis le message");
                    }
                }

                if (userId != null) {
                    Notification notification = new Notification(userId, notificationText);
                    notificationRepository.save(notification);
                    System.out.println("✅ Notification enregistrée pour l'utilisateur ID : " + userId);
                } else {
                    System.out.println("⚠️ userId non trouvé, notification non sauvegardée");
                }

                String subject = "🔔 Action à lancer dans la réunion : " + titre;
                String emailBody = notificationText;
                String email = "responsable@example.com";
                emailService.sendEmail(email, subject, emailBody);
            } else {
                System.out.println("📨 Message texte reçu : " + message);
            }

        } catch (JsonProcessingException e) {
            System.err.println("❌ Erreur de parsing JSON : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Erreur lors du traitement du message Kafka : " + e.getMessage());
            e.printStackTrace();
        }
    }



    private String extractBetween(String message, String start, String end) {
        int startIndex = message.indexOf(start);
        if (startIndex == -1) return "";
        startIndex += start.length();
        int endIndex = message.indexOf(end, startIndex);
        if (endIndex == -1) return "";
        return message.substring(startIndex, endIndex).trim();
    }

    private String extractAfter(String message, String key) {
        int index = message.indexOf(key);
        if (index == -1) return "";
        return message.substring(index + key.length()).trim();
    }

    private List<String> extractUserIdsFromMessage(String message) {
        String prefix = "Réunion créée pour l'utilisateur ";
        List<String> userIds = new ArrayList<>();

        int startIndex = message.indexOf(prefix);
        while (startIndex != -1) {
            String userInfo = message.substring(startIndex + prefix.length()).trim();
            String userIdString = userInfo.replaceAll(".*\\(ID:\\s*(\\d+)\\).*", "$1").trim();

            if (!userIdString.isEmpty()) {
                userIds.add(userIdString);
            }

            startIndex = message.indexOf(prefix, startIndex + 1);
        }

        return userIds;
    }


    private String extractValue(String part, String prefix) {
        if (part != null && part.startsWith(prefix)) {
            String value = part.substring(prefix.length()).trim();
            return value.isEmpty() ? null : value;
        }
        return null;
    }


    private String getEmailByUserId(Long userId) {
        UserDTO user = userClient.getUserById(userId);
        if (user != null && user.getEmail() != null) {
            System.out.println("Email récupéré : " + user.getEmail());
            return user.getEmail();
        } else {
            System.out.println("❌ Erreur : Email non trouvé pour l'utilisateur ID: " + userId);
            return null;
        }
    }

}