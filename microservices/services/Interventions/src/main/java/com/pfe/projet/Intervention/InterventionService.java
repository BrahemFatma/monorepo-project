package com.pfe.projet.Intervention;

import com.pfe.projet.exception.InterventionNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class InterventionService {

    private final InterventionRepository repository;
    private final InterventionMapper mapper;
    private final UserClient userClient;
    private final UserInterventionService userInterventionService;
    @Autowired
    private UserInterventionRepository userInterventionRepository;
    @Autowired
    private ClientServiceClient clientServiceClient;

    @Autowired
    public InterventionService(InterventionRepository repository, InterventionMapper mapper, UserClient userClient, UserInterventionService userInterventionService) {
        this.repository = repository;
        this.mapper = mapper;
        this.userClient = userClient;
        this.userInterventionService = userInterventionService;
    }

    public Long createIntervention(InterventionRequest request) {
        var intervention = repository.save(mapper.toIntervention(request));

        if (request.userId() != null) {
            userInterventionService.addUserToIntervention(request.userId(), intervention.getId());
        }

        return intervention.getId();
    }





    public void updateIntervention(InterventionRequest request) {
        var intervention = repository.findById(request.id())
                .orElseThrow(() -> new InterventionNotFoundException(
                        format("Cannot update Intervention:: No Intervention found with the provided ID :: %s", request.id())
                ));

        mergeIntervention(intervention, request);
        repository.save(intervention);

        // Mise à jour du lien utilisateur si fourni
        if (request.userId() != null) {
            // 🔁 Supprimer les anciens utilisateurs associés
            userInterventionService.removeUsersFromIntervention(intervention.getId());

            // ➕ Ajouter le nouveau user
            userInterventionService.addUserToIntervention(request.userId(), intervention.getId());
        }
    }




    private void mergeIntervention(Interventions intervention, InterventionRequest request) {
        if (request.clientId() != null) {
            intervention.setClientId(request.clientId());
        }
        if (request.date() != null) {
            intervention.setDate(request.date());
        }
        if (request.objectif() != null && !request.objectif().isEmpty()) {
            intervention.setObjectif(request.objectif());
        }
        if (request.heureDebutVisite() != null && !request.heureDebutVisite().isEmpty()) {
            intervention.setHeureDebutVisite(request.heureDebutVisite());
        }
        if (request.heureFinVisite() != null && !request.heureFinVisite().isEmpty()) {
            intervention.setHeureFinVisite(request.heureFinVisite());
        }
        if (request.actionsMiseEnPlace() != null && !request.actionsMiseEnPlace().isEmpty()) {
            intervention.setActionsMiseEnPlace(request.actionsMiseEnPlace());
        }
        if (request.statut() != null) {
            intervention.setStatut(request.statut());
        }
        if (request.typeIntervention() != null) {
            intervention.setTypeIntervention(request.typeIntervention());
        }
        if (request.moyenIntervention() != null) {
            intervention.setMoyenIntervention(request.moyenIntervention());
        }
        if (request.categorieIntervention() != null) {
            intervention.setCategorieIntervention(request.categorieIntervention());
        }
        if (request.natureIntervention() != null) {
            intervention.setNatureIntervention(request.natureIntervention());
        }
        // Attributs booléens (valeurs primitives)
        intervention.setAccesDev(request.accesDev());
        intervention.setFacturation(request.facturation());

        if (request.observationFacturation() != null && !request.observationFacturation().isEmpty()) {
            intervention.setObservationFacturation(request.observationFacturation());
        }

        if (request.utilisateurs() != null && !request.utilisateurs().isEmpty()) {
            List<UtilisateurIntervention> utilisateurs = request.utilisateurs().stream()
                    .map(req -> {
                        UtilisateurIntervention ui = new UtilisateurIntervention();
                        ui.setNomUtilisateur(req.nomUtilisateur());
                        ui.setService(req.service());
                        ui.setPointsADiscuter(req.pointsADiscuter());
                        return ui;
                    })
                    .collect(Collectors.toList());
            intervention.setUtilisateurs(utilisateurs);
        }
    }



    public List<InterventionResponse> findAllInterventions() {
        List<Interventions> interventions = repository.findAll();
        return interventions.stream().map(intervention -> {
            List<UserResponse> users = userInterventionService.getUsersForIntervention(intervention.getId());

            InterventionResponse response = mapper.fromIntervention(intervention);
            response.setResponsable(users);

            return response;
        }).collect(Collectors.toList());
    }

    public boolean existsById(Long interventionId) {
        return repository.findById(interventionId).isPresent();
    }

    public InterventionResponse findById(Long interventionId) {
        Interventions intervention = repository.findById(interventionId)
                .orElseThrow(() -> new InterventionNotFoundException(
                        format("No intervention found with the provided ID:: %s", interventionId)
                ));
        List<UserResponse> users = userInterventionService.getUsersForIntervention(interventionId);
        InterventionResponse response = mapper.fromIntervention(intervention);
        response.setResponsable(users);

        return response;
    }

    public void deleteIntervention(Long interventionId) {
        repository.deleteById(interventionId);
    }
    public List<UserResponse> getUsersByIntervention(Long interventionId) {
        // Vous pouvez utiliser votre service 'userInterventionService' pour récupérer les utilisateurs associés à cette intervention
        List<UserResponse> users = userInterventionService.getUsersForIntervention(interventionId);
        return users;
    }
    public List<Interventions> getInterventionsByClientId(Long clientId) {
        // Récupérer toutes les interventions associées à un client
        List<Interventions> interventions = repository.findByClientId(clientId);  // Assurez-vous que vous avez une méthode qui permet de trouver des interventions par clientId

        // Optionally, fetch client details via FeignClient for each intervention (if needed)
        for (Interventions intervention : interventions) {
            ClientDTO clientDTO = clientServiceClient.getClientById(intervention.getClientId());  // Fetch the client details
            intervention.setClientId(clientDTO.getId());  // Optionally set or attach additional client details
        }

        return interventions;
    }






}
