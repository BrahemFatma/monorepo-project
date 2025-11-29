package com.pfe.projet.notifications;

import org.springframework.stereotype.Service;

@Service
public class NotificationMapper {
    private final UserClient userClient;

    public NotificationMapper(UserClient userClient) {
        this.userClient = userClient;
    }

    public Notification toNotification(NotificationRequest request) {
        if (request == null) {
            return null;
        }

        Notification notification = new Notification();
        notification.setId(request.id());
        notification.setMessage(request.message());
        notification.setDateEnvoi(request.dateEnvoi());

        return notification;
    }
    public NotificationResponse fromNotification(Notification notification) {
        if (notification == null) {
            return null;
        }
        UserDTO user = userClient.getUserById(notification.getUserId());
        return new NotificationResponse(
                notification.getId(),
                notification.getMessage(),
                notification.getDateEnvoi(),
                user
        );
    }


}
