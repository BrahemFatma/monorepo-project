package com.pfe.projet.Routine;

import jakarta.validation.constraints.NotNull;
import java.util.Date;

public record RoutineRequest(
        Long id,

        @NotNull(message = "Routine name is required")
        String nom,

        @NotNull(message = "Routine description is required")
        String description,

        @NotNull(message = "Routine dateDebut is required")
        Date dateDebut,

        @NotNull(message = "Routine dateFin is required")
        Date dateFin,

        @NotNull(message = "Routine status is required")
        String statut,

        Long userId,

        Long clientId,   // Nouveau champ

        String projet    // Nouveau champ
) {
}
