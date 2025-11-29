package com.pfe.projet.notifications;

public record ReunionNotificationRequest(
        String titre,
        String date,
        String heure,
        String lieu,
        String documents,
        String Usernom,
        String Userprenom,
        String Useremail
) {
}
