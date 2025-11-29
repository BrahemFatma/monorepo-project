package com.pfe.projet.reunion;

import java.util.Date;

public record ReunionResponse(
        String id,
        String titre,
        String description,
        Date date,
        String heure,
        String lieu
) {


}
