package com.pfe.projet.notifications;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:8090")
public interface UserClient {

    @GetMapping("/api/v1/Users/{id}")
    UserDTO getUserById(@PathVariable Long id);
}