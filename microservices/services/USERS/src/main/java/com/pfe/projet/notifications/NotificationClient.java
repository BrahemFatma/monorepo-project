package com.pfe.projet.notifications;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "notification-service", url = "http://localhost:8050/api/v1/Notifications")
public interface NotificationClient {
    @GetMapping("/{notificationId}")
    Optional<NotificationResponse> getNotificationById(@PathVariable Long notificationId);
}
