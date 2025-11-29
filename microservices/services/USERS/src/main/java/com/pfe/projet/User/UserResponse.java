package com.pfe.projet.User;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pfe.projet.interventions.InterventionResponse;
import com.pfe.projet.reunion.ReunionResponse;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Set;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    public String role;
    public String reunionId;
    public ReunionResponse reunion;



    public UserResponse(Long id, String nom, String prenom, String email, String motDePasse, String role,
                        String reunionId, ReunionResponse reunion) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
        this.reunionId = reunionId;
        this.reunion = reunion;

    }
    public UserResponse(Long id, String nom, String prenom, String email, String motDePasse, String role) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
    }
    // Getters
    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public String getRole() {
        return role;
    }



    public ReunionResponse getReunion() {
        return reunion;
    }

    public String getReunionId() {
        return reunionId;
    }




    @Override
    public String toString() {
        return "UserResponse{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", motDePasse='" + motDePasse + '\'' +
                ", role='" + role + '\'' +
                ", reunionId='" + reunionId + '\'' +
                ", reunion=" + reunion +
                '}';
    }


    public UserResponse withReunionId(String newReunionId) {
        return new UserResponse(id, nom, prenom, email, motDePasse, role, newReunionId, reunion);
    }
}
