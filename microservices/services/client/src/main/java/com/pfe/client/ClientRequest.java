package com.pfe.client;

import jakarta.validation.constraints.NotNull;

public record ClientRequest(
        Long id,

        @NotNull(message = "Le nom est obligatoire")
        String nom,

        @NotNull(message = "Le mail est obligatoire")
        String mail,

        @NotNull(message = "Le numéro de téléphone est obligatoire")
        String numeroTelephone,

        @NotNull(message = "L'adresse est obligatoire")
        String adresse
) {
}
