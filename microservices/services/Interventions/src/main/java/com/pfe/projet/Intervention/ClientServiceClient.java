package com.pfe.projet.Intervention;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "client-service", url = "http://localhost:8020/api/v1/Client")
public interface ClientServiceClient {

    @GetMapping("/{clientId}")
    ClientDTO getClientById(@PathVariable Long clientId);
}

