package com.pfe.projet.Intervention;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InterventionMapper {

    public Interventions toIntervention(InterventionRequest request) {
        if (request == null) {
            return null;
        }

        Interventions intervention = new Interventions();
        intervention.setId(request.id());
        intervention.setClientId(request.clientId());
        intervention.setDate(request.date());
        intervention.setObjectif(request.objectif());
        intervention.setHeureDebutVisite(request.heureDebutVisite());
        intervention.setHeureFinVisite(request.heureFinVisite());
        intervention.setActionsMiseEnPlace(request.actionsMiseEnPlace());
        intervention.setStatut(request.statut());

        intervention.setTypeIntervention(request.typeIntervention());
        intervention.setMoyenIntervention(request.moyenIntervention());
        intervention.setCategorieIntervention(request.categorieIntervention());
        intervention.setNatureIntervention(request.natureIntervention());
        intervention.setAccesDev(request.accesDev());
        intervention.setFacturation(request.facturation());
        intervention.setObservationFacturation(request.observationFacturation());

        if (request.utilisateurs() != null) {
            List<UtilisateurIntervention> utilisateurs = request.utilisateurs().stream()
                    .map(dto -> {
                        UtilisateurIntervention u = new UtilisateurIntervention();
                        u.setNomUtilisateur(dto.nomUtilisateur());
                        u.setService(dto.service());
                        u.setPointsADiscuter(dto.pointsADiscuter());
                        return u;
                    })
                    .collect(Collectors.toList());
            intervention.setUtilisateurs(utilisateurs);
        }

        return intervention;
    }

    public InterventionResponse fromIntervention(Interventions intervention) {
        if (intervention == null) {
            return null;
        }

        List<UtilisateurInterventionResponse> utilisateursResponse = null;
        if (intervention.getUtilisateurs() != null) {
            utilisateursResponse = intervention.getUtilisateurs().stream()
                    .map(u -> new UtilisateurInterventionResponse(
                            u.getNomUtilisateur(),
                            u.getService(),
                            u.getPointsADiscuter()
                    ))
                    .collect(Collectors.toList());
        }

        List<UserResponse> responsableList = new ArrayList<>();
        // Ajouter les responsables si nécessaire

        return new InterventionResponse(
                intervention.getId(),
                intervention.getClientId(),
                intervention.getDate(),
                intervention.getObjectif(),
                intervention.getStatut(),
                intervention.getHeureDebutVisite(),
                intervention.getHeureFinVisite(),
                intervention.getActionsMiseEnPlace(),
                responsableList,
                utilisateursResponse,
                intervention.getTypeIntervention(),
                intervention.getMoyenIntervention(),
                intervention.getCategorieIntervention(),
                intervention.getNatureIntervention(),
                intervention.isAccesDev(),
                intervention.isFacturation(),
                intervention.getObservationFacturation()
        );
    }
}
