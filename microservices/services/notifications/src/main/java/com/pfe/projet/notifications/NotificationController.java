package com.pfe.projet.notifications;

import feign.FeignException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/Notifications")
public class NotificationController {

    private final NotificationService service;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UserClient userClient;
    @Autowired
    private CsvExportService csvExportService;
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @GetMapping("/export-notifications/csv")
    public ResponseEntity<byte[]> exportNotificationsToCsv() throws IOException {
        byte[] csvData = csvExportService.exportNotificationsToCsv();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=notifications.csv")
                .header(HttpHeaders.CONTENT_TYPE, "text/csv")
                .body(csvData);
    }

    @PostMapping
    public ResponseEntity<Long> createNotification(
            @RequestBody @Valid NotificationRequest request
    ) {
        return ResponseEntity.ok(service.createNotification(request));
    }

    @PutMapping
    public ResponseEntity<Void> updateNotification(
            @RequestBody @Valid NotificationRequest request
    ){
        service.updateNotification(request);
        return ResponseEntity.accepted().build();
    }


    @GetMapping
    public ResponseEntity<List<NotificationResponse>> findAll() {
        List<NotificationResponse> notifications = service.findAllNotifications();
        System.out.println("Notifications: " + notifications);

        if (notifications.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/exist/{notification-id}")
    public ResponseEntity<Boolean> existsById(
            @PathVariable("notification-id") Long notificationId
    ) {
        return ResponseEntity.ok(service.existsById(notificationId));
    }

    @GetMapping("/{notification-id}")
    public ResponseEntity<NotificationResponse> findById(
            @PathVariable("notification-id") Long notificationId
    ) {
        return ResponseEntity.ok(service.findById(notificationId));
    }

    @DeleteMapping("/{notification-id}")
    public ResponseEntity<Void> delete(
            @PathVariable("notification-id") Long notificationId
    ) {
        service.deleteNotification(notificationId);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/send")
    public ResponseEntity<NotificationResponse> sendNotification(@RequestBody Notification notification) {
        UUID requestId = UUID.randomUUID();
        System.out.println("📩 [" + requestId + "] Requête reçue : " + notification);

        if (notification.getUserId() == null || notification.getUserId() != 13) {
            System.out.println("❌ [" + requestId + "] Erreur : userId doit être 13, mais reçu : " + notification.getUserId());

            NotificationResponse errorResponse = new NotificationResponse(null, "Le userId doit être 13", null, null);
            return ResponseEntity.badRequest().body(errorResponse);
        }

        try {
            UserDTO user = userClient.getUserById(notification.getUserId());

            if (user == null) {
                System.out.println("❌ [" + requestId + "] Utilisateur non trouvé pour l'ID 13");

                NotificationResponse errorResponse = new NotificationResponse(null, "Utilisateur non trouvé", null, null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            System.out.println("🔴 Tentative de sauvegarde de la notification");
            Notification savedNotification = notificationRepository.save(notification);
            System.out.println("🔵 Notification sauvegardée : " + savedNotification);
            NotificationResponse response = new NotificationResponse(
                    savedNotification.getId(),
                    savedNotification.getMessage(),
                    savedNotification.getDateEnvoi(),
                    user
            );

            sendEmailToUser(user, savedNotification.getMessage());

            return ResponseEntity.ok(response);
        } catch (FeignException.NotFound e) {
            e.printStackTrace();

            NotificationResponse errorResponse = new NotificationResponse(null, "Erreur interne du serveur", null, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        } catch (Exception e) {
            e.printStackTrace();

            NotificationResponse errorResponse = new NotificationResponse(null, "Erreur interne du serveur", null, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    private void sendEmailToUser(UserDTO user, String notificationMessage) {
        try {
            if (user == null || user.getEmail() == null) {
                System.out.println("Erreur : Email utilisateur invalide");
                return;
            }
            System.out.println("📨 Email envoyé à : " + user.getId());

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Notification de réunion");
            mailMessage.setText("Bonjour " + user.getPrenom() + ",\n\n" +
                    "Vous avez une nouvelle notification :\n" + notificationMessage + "\n\n" +
                    "Cordialement,\nVotre équipe de notification");

            mailSender.send(mailMessage);
            System.out.println("📨 Email envoyé à : " + user.getEmail());
        } catch (Exception e) {
            System.out.println("Erreur lors de l'envoi de l'email : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
