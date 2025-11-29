package com.pfe.projet.notifications;

import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

@Service
public class CsvExportService {

    @Autowired
    private NotificationService notificationService;

    public byte[] exportNotificationsToCsv() throws IOException {
        List<NotificationResponse> notificationResponses = notificationService.findAllNotifications();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(byteArrayOutputStream))) {
            // Header (titres des colonnes)
            writer.writeNext(new String[]{"ID", "Message", "Date Envoi", "User ID", "User Name"});

            // Data rows
            for (NotificationResponse notificationResponse : notificationResponses) {
                String[] row = {
                        notificationResponse.getId().toString(),
                        notificationResponse.getMessage(),
                        notificationResponse.getDateEnvoi().toString(),
                        notificationResponse.getUser().getId().toString(),
                        notificationResponse.getUser().getNom() + " " + notificationResponse.getUser().getPrenom()
                };
                writer.writeNext(row);
            }
        }

        return byteArrayOutputStream.toByteArray();
    }
}
