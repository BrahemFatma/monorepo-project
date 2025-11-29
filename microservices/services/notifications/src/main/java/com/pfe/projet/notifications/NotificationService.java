package com.pfe.projet.notifications;

import com.pfe.projet.exception.NotificationNotFoundException;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service

public class NotificationService {
    private final KafkaTemplate<String, ReunionNotificationRequest> kafkaTemplate;
    private final NotificationRepository repository;
    private final NotificationMapper mapper;
    private final UserClient userClient;

    public UserDTO fetchUser(Long userId) {
        return userClient.getUserById(userId);
    }
    @Autowired
    public NotificationService(KafkaTemplate<String, ReunionNotificationRequest> kafkaTemplate, NotificationRepository repository, NotificationMapper mapper, UserClient userClient) {
        this.kafkaTemplate = kafkaTemplate;
        this.repository = repository;
        this.mapper = mapper;

        this.userClient = userClient;
    }

    public Long createNotification(NotificationRequest request) {
        var notification = repository.save(mapper.toNotification(request));
        return notification.getId();
    }

    public void updateNotification(NotificationRequest request) {
        var notification = repository.findById(request.id())
                .orElseThrow(() -> new NotificationNotFoundException(
                        format("Cannot update notification: No notification found with the provided ID :: %s", request.id())
                ));
        mergeNotification(notification, request);
        repository.save(notification);
    }

    private void mergeNotification(Notification notification, NotificationRequest request) {
        if (StringUtils.isNotBlank(request.message())) {
            notification.setMessage(request.message());
        }
        if (request.dateEnvoi() != null) {
            notification.setDateEnvoi(request.dateEnvoi());
        }
    }

    public List<NotificationResponse> findAllNotifications() {
        List<Notification> notifications = repository.findAll();
        if (notifications.isEmpty()) {
            System.out.println("No notifications found");
        }
        return notifications.stream()
                .map(mapper::fromNotification)
                .collect(Collectors.toList());
    }

    public boolean existsById(Long notificationId) {
        return repository.findById(notificationId)
                .isPresent();
    }

    public NotificationResponse findById(Long notificationId) {
        return repository.findById(notificationId)
                .map(mapper::fromNotification)
                .orElseThrow(() -> new NotificationNotFoundException(
                        format("No notification found with the provided ID:: %s", notificationId)
                ));
    }

    public void deleteNotification(Long notificationId) {
        repository.deleteById(notificationId);
    }
}
