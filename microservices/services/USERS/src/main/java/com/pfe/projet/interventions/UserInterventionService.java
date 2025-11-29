package com.pfe.projet.interventions;



import com.pfe.projet.User.UserResponse;
import com.pfe.projet.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
public class UserInterventionService {

    @Autowired
    private RestTemplate restTemplate;
    private final String USER_URL = "http://localhost:8090/api/v1/Users";

    private final String INTERVENTION_URL = "http://localhost:8030/api/v1/Interventions";

    public UserInterventionsResponseDTO getUserInterventions(Long userId) {
        // Fetch user details
        UserResponse user = restTemplate.getForObject(USER_URL + "/" + userId, UserResponse.class);

        if (user == null) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
        InterventionDTO[] interventions = restTemplate.getForObject(INTERVENTION_URL + "?userId=" + userId, InterventionDTO[].class);
        return new UserInterventionsResponseDTO(
                user.getId(),
                user.getNom(),
                user.getPrenom(),
                user.getEmail(),
                List.of(interventions)
        );
    }
}

