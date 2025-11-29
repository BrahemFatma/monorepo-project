package com.pfe.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public Long createClient(Client client) {
        Client savedClient = clientRepository.save(client);
        return savedClient.getId();
    }

    public void updateClient(Client client) {
        clientRepository.save(client);
    }

    public List<Client> findAllClients() {
        return clientRepository.findAll();
    }

    public Client findClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec ID: " + id));
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }
}
