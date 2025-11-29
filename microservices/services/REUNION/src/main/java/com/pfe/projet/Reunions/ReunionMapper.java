package com.pfe.projet.Reunions;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReunionMapper {

    public Reunion toReunion(ReunionRequest request) {
        if (request == null) {
            return null;
        }

        List<Long> userIds = request.userIds() != null ? request.userIds().stream().collect(Collectors.toList()) : null;

        return new Reunion(
                request.id(),
                request.titre(),
                request.description(),
                request.date(),
                request.heure(),
                request.lieu(),
                request.pv(),
                request.documents(),
                userIds,
                request.type(),
                request.clientId(),
                request.dateFin(),
                request.heureFin(),
                request.dureePrevue(),
                request.dureeReelle(),
                request.actionMiseEnPlace(),
                request.descriptionAction(), // Ajouté ici
                request.dateAction(),
                request.responsableActionId(),
                request.clotureAction(),
                request.dateCloture()
        );
    }

    public ReunionResponse fromReunion(Reunion reunion) {
        if (reunion == null) {
            return null;
        }

        return new ReunionResponse(
                reunion.getId(),
                reunion.getTitre(),
                reunion.getDescription(),
                reunion.getDate(),
                reunion.getHeure(),
                reunion.getLieu(),
                reunion.getPv(),
                reunion.getDocuments(),
                reunion.getUserIds(),
                reunion.getType(),
                reunion.getClientId(),
                reunion.getDateFin(),
                reunion.getHeureFin(),
                reunion.getDureePrevue(),
                reunion.getDureeReelle(),
                reunion.getActionMiseEnPlace(),
                reunion.getDescriptionAction(), // Ajouté ici
                reunion.getDateAction(),
                reunion.getResponsableActionId(),
                reunion.getClotureAction(),
                reunion.getDateCloture()
        );
    }
}
