package com.pfe.projet.Intervention;

import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CsvExportService {

    @Autowired
    private InterventionService interventionService;

    @Autowired
    private ClientServiceClient clientServiceClient;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public byte[] exportInterventionsToCsv() throws IOException {
        List<InterventionResponse> interventions = interventionService.findAllInterventions();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(byteArrayOutputStream))) {
            writer.writeNext(new String[] {
                    "ID", "Client", "Date", "Objectif", "Heure Début", "Heure Fin",
                    "Actions Mise en Place", "Statut", "Responsables", "Utilisateurs (Nom - Service - Points)"
            });

            for (InterventionResponse intervention : interventions) {
                String formattedDate = DATE_FORMAT.format(intervention.getDate());

                String responsables = intervention.getResponsable().stream()
                        .map(UserResponse::getNom)
                        .collect(Collectors.joining(", "));


                String utilisateursDetails = intervention.getUtilisateurs().stream()
                        .map(util -> util.getNomUtilisateur() + " - " + util.getService()
                                + " - Points: " + String.join(" / ", util.getPointsADiscuter()))
                        .collect(Collectors.joining(" | "));


                String[] row = {
                        String.valueOf(intervention.getId()),
                        getClientName(intervention.getClientId()),
                        formattedDate,
                        intervention.getObjectif(),
                        intervention.getHeureDebutVisite(),
                        intervention.getHeureFinVisite(),
                        intervention.getActionsMiseEnPlace(),
                        intervention.getStatut().toString(),
                        responsables,
                        utilisateursDetails
                };

                writer.writeNext(row);
            }
        }

        return byteArrayOutputStream.toByteArray();
    }

    private String getClientName(Long clientId) {
        try {
            ClientDTO clientDTO = clientServiceClient.getClientById(clientId);
            return clientDTO != null ? clientDTO.getNom() : "Client not found";
        } catch (Exception e) {
            // Log the exception (use proper logging)
            return "Error fetching client";
        }
    }

}
