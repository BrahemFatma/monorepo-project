package com.pfe.client;

import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

@Service
public class ClientCsvService {

    @Autowired
    private ClientService clientService;

    public byte[] exportClientsToCsv() throws IOException {
        List<Client> clients = clientService.findAllClients();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(byteArrayOutputStream))) {
            // Header
            writer.writeNext(new String[]{"ID", "Nom", "Mail", "Numéro de Téléphone", "Adresse"});

            // Data
            for (Client client : clients) {
                String[] row = {
                        String.valueOf(client.getId()),
                        client.getNom(),
                        client.getMail(),
                        client.getNumeroTelephone(),
                        client.getAdresse()
                };
                writer.writeNext(row);
            }
        }

        return byteArrayOutputStream.toByteArray();
    }
}
