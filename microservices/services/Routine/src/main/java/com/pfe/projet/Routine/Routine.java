package com.pfe.projet.Routine;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Routine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String description;
    private Date dateDebut;
    private Date dateFin;
    private Long userId;
    private Long clientId;
    private String projet;
    @Enumerated(EnumType.STRING)
    private StatutRoutine statut;


    public Routine() {}


    // Constructeur avec tous les attributs
    public Routine(Long id, String nom, String description, Date dateDebut, Date dateFin, Long userId,
                   Long clientId, String projet, StatutRoutine statut) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.userId = userId;
        this.clientId = clientId;
        this.projet = projet;
        this.statut = statut;
    }
    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getProjet() {
        return projet;
    }

    public void setProjet(String projet) {
        this.projet = projet;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public StatutRoutine getStatut() {
        return statut;
    }

    public void setStatut(StatutRoutine statut) {
        this.statut = statut;
    }
}
