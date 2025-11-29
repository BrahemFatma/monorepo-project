package com.pfe.client;

import org.springframework.stereotype.Service;

@Service
public class ClientMapper {

    public Client toClient(ClientRequest request) {
        if (request == null) {
            return null;
        }

        Client client = new Client();
        client.setId(request.id());
        client.setNom(request.nom());
        client.setMail(request.mail());
        client.setNumeroTelephone(request.numeroTelephone());
        client.setAdresse(request.adresse());

        return client;
    }

    public ClientResponse fromClient(Client client) {
        if (client == null) {
            return null;
        }

        return new ClientResponse(
                client.getId(),
                client.getNom(),
                client.getMail(),
                client.getNumeroTelephone(),
                client.getAdresse()
        );
    }
}
