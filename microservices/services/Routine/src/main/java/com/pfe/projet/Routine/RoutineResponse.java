package com.pfe.projet.Routine;

import com.pfe.projet.user.UserResponse;

import java.util.Date;

public record RoutineResponse(
        Long id,
        String nom,
        String description,
        Date dateDebut,
        Date dateFin,
        StatutRoutine statut,
        Long userId,
        UserResponse user,
        Long clientId,       // Nouveau champ
        String projet        // Nouveau champ
) {
}
