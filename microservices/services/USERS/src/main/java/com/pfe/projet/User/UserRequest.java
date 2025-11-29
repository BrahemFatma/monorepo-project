package com.pfe.projet.User;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record UserRequest(
        Long id,
        @NotNull(message = "User name is required")
        String nom,
        @NotNull(message = "User lastname is required")
        String prenom,

        @Email(message = "User email is not a valid email adress")
        String email,
        @NotNull(message = "User password is required")
        String motDePasse,

        Role role,
        String reunionId
) {
}