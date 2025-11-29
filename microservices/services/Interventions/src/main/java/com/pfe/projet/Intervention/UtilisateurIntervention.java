package com.pfe.projet.Intervention;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class UtilisateurIntervention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomUtilisateur;
    private String service;

    @ElementCollection
    private List<String> pointsADiscuter;

    // Getters et setters
    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }

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
