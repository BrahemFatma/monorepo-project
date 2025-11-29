package com.pfe.projet.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@FeignClient(
        name="USERS-service",
        url="http://localhost:8090/api/v1/Users"

)

public interface UserClient {

        @GetMapping("/{user-id}")
        Optional<UserResponse> findUserById(@PathVariable("user-id") String userId);
   }



