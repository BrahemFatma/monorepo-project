package com.pfe.projet.Reunions;

import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

public record ReunionRequest(
        Long id,
        @NotNull(message = "Reunion title is required") String titre,
        @NotNull(message = "Reunion description is required") String description,
        @NotNull(message = "Reunion date is required") Date date,
        @NotNull(message = "Reunion heure is required") String heure,
        @NotNull(message = "Reunion lieu is required") String lieu,
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
) {}
