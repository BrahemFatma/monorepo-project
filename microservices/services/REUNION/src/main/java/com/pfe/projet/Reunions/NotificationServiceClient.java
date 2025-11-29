package com.pfe.projet.Reunions;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service", url = "http://localhost:8050/api/v1/Notifications")
public interface NotificationServiceClient {

    @PostMapping("/send")
    Long createNotification(@RequestBody NotificationRequest request);
}
