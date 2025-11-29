package com.pfe.projet.notifications;

import jakarta.validation.constraints.NotNull;

public record NotificationRequest(
        Long id,

        @NotNull(message = "Notification message is required")
        String message,

        @NotNull(message = "Notification date of sending is required")
        java.util.Date dateEnvoi
) {
}
