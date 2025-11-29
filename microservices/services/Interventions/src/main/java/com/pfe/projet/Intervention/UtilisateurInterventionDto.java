package com.pfe.projet.Intervention;

import java.util.List;

public class UtilisateurInterventionDto {

    private String nomUtilisateur;
    private String service;
    private List<String> pointsADiscuter;

    // Constructeurs
    public UtilisateurInterventionDto() {
    }

    public UtilisateurInterventionDto(String nomUtilisateur, String service, List<String> pointsADiscuter) {
        this.nomUtilisateur = nomUtilisateur;
        this.service = service;
        this.pointsADiscuter = pointsADiscuter;
    }

    // Getters & Setters
    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public List<String> getPointsADiscuter() {
        return pointsADiscuter;
    }

    public void setPointsADiscuter(List<String> pointsADiscuter) {
        this.pointsADiscuter = pointsADiscuter;
    }
}
