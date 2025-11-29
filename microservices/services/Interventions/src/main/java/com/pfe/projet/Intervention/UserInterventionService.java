package com.pfe.projet.Intervention;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserInterventionService {

    @Autowired
    private UserInterventionRepository userInterventionRepository;

    @Autowired
    private UserClient userClient;

    public void addUserToIntervention(Long userId, Long interventionId) {
        UserDTO user = userClient.getUserById(userId);

        if (user != null) {
            UserIntervention userIntervention = new UserIntervention(userId, interventionId);
            userInterventionRepository.save(userIntervention);
        } else {
            throw new RuntimeException("Utilisateur non trouvé");
        }
    }
    public void removeUsersFromIntervention(Long interventionId) {
        List<UserIntervention> userInterventions = userInterventionRepository.findByInterventionId(interventionId);
        userInterventionRepository.deleteAll(userInterventions);
    }

    public List<UserResponse> getUsersForIntervention(Long interventionId) {
        List<UserIntervention> userInterventions = userInterventionRepository.findByInterventionId(interventionId);

        return userInterventions.stream()
                .map(userIntervention -> {
                    UserDTO userDTO = userClient.getUserById(userIntervention.getUserId());

                    return UserResponse.builder()
                            .id(userDTO.getId())
                            .nom(userDTO.getNom())
                            .prenom(userDTO.getPrenom())
                            .email(userDTO.getEmail())
                            .role(userDTO.getRole())
                            .build();
                })
                .collect(Collectors.toList());
    }

}
