package com.pfe.projet.Intervention;

import com.pfe.projet.Intervention.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserClient {

    @Autowired
    private RestTemplate restTemplate;

    private static final String USER_SERVICE_URL = "http://localhost:8090/api/v1/Users";

    public UserDTO getUserById(Long userId) {
        return restTemplate.getForObject(USER_SERVICE_URL + "/" + userId, UserDTO.class);
    }
}