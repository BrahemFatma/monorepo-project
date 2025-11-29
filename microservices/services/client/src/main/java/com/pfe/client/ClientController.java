package com.pfe.client;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/Client")
public class ClientController {

    private final ClientService clientService;
    private final ClientCsvService clientCsvService;
    @Autowired
    public ClientController(ClientService clientService, ClientCsvService clientCsvService) {
        this.clientService = clientService;
        this.clientCsvService = clientCsvService;
    }

    @GetMapping("/export-clients/csv")
    public ResponseEntity<byte[]> exportClientsToCsv() throws IOException {
        byte[] csvData = clientCsvService.exportClientsToCsv();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=clients.csv")
                .header(HttpHeaders.CONTENT_TYPE, "text/csv")
                .body(csvData);
    }

    @PostMapping
    public ResponseEntity<Long> createClient(@RequestBody @Valid Client client) {
        return ResponseEntity.ok(clientService.createClient(client));
    }

    @PutMapping("/{clientId}")
    public ResponseEntity<Void> updateClient(
            @PathVariable Long clientId,
            @RequestBody @Valid Client client
    ) {
        // Mise à jour du client avec tous les paramètres nécessaires
        Client updatedClient = new Client(
                clientId,                // Utilisation de l'ID du client à mettre à jour
                client.getNom(),         // Récupère les autres informations depuis le corps de la requête
                client.getMail(),
                client.getNumeroTelephone(),
                client.getAdresse()
        );

        clientService.updateClient(updatedClient);
        return ResponseEntity.accepted().build();
    }


    @GetMapping
    public ResponseEntity<List<Client>> findAllClients() {
        List<Client> clients = clientService.findAllClients();

        if (clients.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{client-id}")
    public ResponseEntity<Client> findClientById(@PathVariable("client-id") Long clientId) {
        return ResponseEntity.ok(clientService.findClientById(clientId));
    }

    @DeleteMapping("/{client-id}")
    public ResponseEntity<Void> deleteClient(@PathVariable("client-id") Long clientId) {
        clientService.deleteClient(clientId);
        return ResponseEntity.accepted().build();
    }
}
