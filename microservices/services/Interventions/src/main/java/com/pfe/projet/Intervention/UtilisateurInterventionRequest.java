package com.pfe.projet.Intervention;

import java.util.List;

public record UtilisateurInterventionRequest(
        Long idUtilisateur,
        String nomUtilisateur,
        String service,
        List<String> pointsADiscuter
) {}
