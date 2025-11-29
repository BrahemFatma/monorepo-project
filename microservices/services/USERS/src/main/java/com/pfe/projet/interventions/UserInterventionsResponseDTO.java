package com.pfe.projet.interventions;

import java.util.List;

public class UserInterventionsResponseDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private List<InterventionDTO> interventions;

    public List<InterventionDTO> getInterventions() {
        return interventions;
    }

    public void setInterventions(List<InterventionDTO> interventions) {
        this.interventions = interventions;
    }

    public UserInterventionsResponseDTO(Long id, String nom, String prenom, String email, List<InterventionDTO> interventions) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.interventions = interventions;
    }
}
