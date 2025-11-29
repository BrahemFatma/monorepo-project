package com.pfe.projet.Reunions;

import java.util.Date;
import java.util.List;

public record ReunionResponse(
        Long id,
        String titre,
        String description,
        Date date,
        String heure,
        String lieu,
        String pv,
        String documents,
        List<Long> userIds,

        TypeReunion type,
        Long clientId,

        Date dateFin,
        String heureFin,

        String dureePrevue,
        String dureeReelle,

        Boolean actionMiseEnPlace,
        String descriptionAction, // Ajouté ici
        Date dateAction,
        Long responsableActionId,

        Boolean clotureAction,
        Date dateCloture
) {
}
