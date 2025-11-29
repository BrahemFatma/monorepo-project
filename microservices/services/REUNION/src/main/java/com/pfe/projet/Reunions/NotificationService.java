package com.pfe.projet.Reunions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class NotificationService {

    private final NotificationServiceClient notificationServiceClient;

    @Autowired
    public NotificationService(NotificationServiceClient notificationServiceClient) {
        this.notificationServiceClient = notificationServiceClient;
    }

}
