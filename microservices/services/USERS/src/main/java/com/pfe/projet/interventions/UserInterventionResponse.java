package com.pfe.projet.interventions;

public class UserInterventionResponse {

    private Long userId;
    private String nom;
    private String prenom;
    private String email;
    private Long interventionId;
    private String client;
    private String description;

    public UserInterventionResponse(Long userId, String nom, String prenom, String email,
                                    Long interventionId, String client, String description) {
        this.userId = userId;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.interventionId = interventionId;
        this.client = client;
        this.description = description;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getInterventionId() {
        return interventionId;
    }

    public void setInterventionId(Long interventionId) {
        this.interventionId = interventionId;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
