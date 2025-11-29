package com.pfe.projet.Reunions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;  // Import missing Collectors

@Service
public class UserService {

    private final RestTemplate restTemplate;
    private static final String USER_SERVICE_URL = "http://localhost:8090/api/v1/Users";

    @Autowired
    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    public List<UserResponse> findByReunionId(String reunionId) {
        String url = USER_SERVICE_URL + "/reunion/{reunionId}/participants";
        UserResponse[] users = restTemplate.getForObject(url, UserResponse[].class, reunionId);

        if (users == null || users.length == 0) {
            throw new RuntimeException("No participants found for the given reunionId: " + reunionId);
        }

        return List.of(users);
    }

    public List<UserResponse> getUsersByIds(List<Long> userIds) {
        return userIds.stream()
                .map(userId -> getUserById(userId))
                .collect(Collectors.toList());
    }
    public UserResponse getUserById(Long userId) {
        String url = USER_SERVICE_URL + "/{userId}";
        return restTemplate.getForObject(url, UserResponse.class, userId);
    }
    public String getUserEmail(Long userId) {
        UserResponse user = getUserById(userId);
        return user != null ? user.getEmail() : null;
    } public String getNom(Long userId) {
        UserResponse user = getUserById(userId);
        return user != null ? user.getNom() : null;
    }

    // Method to get user's first name (prenom) by ID
    public String getPrenom(Long userId) {
        UserResponse user = getUserById(userId);
        return user != null ? user.getPrenom() : null;
    }
    // À insérer dans la classe UserService


}
