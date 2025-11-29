package com.pfe.projet.Reunions;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("user-service")
public interface UserServiceClient {
    @GetMapping("/users/{userId}")
    UserResponse getUserById(@PathVariable("userId") Long userId);

}
