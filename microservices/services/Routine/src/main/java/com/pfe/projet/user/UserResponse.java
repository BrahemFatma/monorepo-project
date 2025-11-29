package com.pfe.projet.user;

import jakarta.persistence.Column;

public record UserResponse(
         Long id,

         String nom,

         String prenom,

         String email

) {
}
