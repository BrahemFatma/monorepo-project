package com.pfe.projet.Intervention;

import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.Set;

public record InterventionRequest(
        Long id,
        @NotNull Long clientId,
        @NotNull Date date,
        @NotNull String objectif,
        @NotNull StatutIntervention statut,
        String heureDebutVisite,
        String heureFinVisite,
        String actionsMiseEnPlace,
        Long userId,
        Set<UtilisateurInterventionRequest> utilisateurs,
        TypeIntervention typeIntervention,
        MoyenIntervention moyenIntervention,
        CategorieIntervention categorieIntervention,
        NatureIntervention natureIntervention,
        boolean accesDev,
        boolean facturation,
        String observationFacturation
) {}
