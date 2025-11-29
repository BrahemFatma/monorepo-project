package com.pfe.projet.User;

import com.pfe.projet.reunion.ReunionResponse;
import com.pfe.projet.reunion.reunionclient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserMapper {
    private final reunionclient routineclient;

    public UserMapper(reunionclient routineclient) {
        this.routineclient = routineclient;
    }

    public User toUser(UserRequest request) {
        if (request == null) {
            return null;
        }
        return new User(
                request.id(),
                request.nom(),
                request.prenom(),
                request.email(),
                request.motDePasse(),
                request.role(),
                request.reunionId(),
                new ArrayList<>()
        );
    }


    public UserResponse fromUser(User user) {
        if (user == null) {
            return null;
        }

        // Récupération de la réunion associée via Feign Client
        ReunionResponse reunion = null;
        if (user.getReunionId() != null) {
            reunion = routineclient.findReunionById(user.getReunionId().toString()).orElse(null);
        }

        // Création de l'objet UserResponse
        return new UserResponse(
                user.getId(),
                user.getNom(),
                user.getPrenom(),
                user.getEmail(),
                user.getMotDePasse(),
                user.getRole().toString(),
                user.getReunionId(),
                reunion
        );
    }
}
