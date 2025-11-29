package com.pfe.projet.Reunions;

import java.util.List;

public record ReunionConfirmation(
        String titre,
        String date,
        String heure,
        String lieu,
        String documents,
        List<Long> userIds,
        String type

) {
}
