package com.pfe.projet.Intervention;

import java.util.Date;
import java.util.List;

public class InterventionResponse {
    private Long id;
    private Long clientId;
    private Date date;
    private String objectif;
    private String heureDebutVisite;
    private String heureFinVisite;
    private String actionsMiseEnPlace;
    private StatutIntervention statut;

    private TypeIntervention typeIntervention;
    private MoyenIntervention moyenIntervention;
    private CategorieIntervention categorieIntervention;
    private NatureIntervention natureIntervention;
    private boolean accesDev;
    private boolean facturation;
    private String observationFacturation;

    private List<UserResponse> responsable;
    private List<UtilisateurInterventionResponse> utilisateurs;

    // Constructeur
    public InterventionResponse(Long id, Long clientId, Date date, String objectif,
                                StatutIntervention statut, String heureDebutVisite,
                                String heureFinVisite, String actionsMiseEnPlace,
                                List<UserResponse> responsable,
                                List<UtilisateurInterventionResponse> utilisateurs,
                                TypeIntervention typeIntervention, MoyenIntervention moyenIntervention,
                                CategorieIntervention categorieIntervention, NatureIntervention natureIntervention,
                                boolean accesDev, boolean facturation, String observationFacturation) {
        this.id = id;
        this.clientId = clientId;
        this.date = date;
        this.objectif = objectif;
        this.statut = statut;
        this.heureDebutVisite = heureDebutVisite;
        this.heureFinVisite = heureFinVisite;
        this.actionsMiseEnPlace = actionsMiseEnPlace;
        this.responsable = responsable;
        this.utilisateurs = utilisateurs;
        this.typeIntervention = typeIntervention;
        this.moyenIntervention = moyenIntervention;
        this.categorieIntervention = categorieIntervention;
        this.natureIntervention = natureIntervention;
        this.accesDev = accesDev;
        this.facturation = facturation;
        this.observationFacturation = observationFacturation;
    }

    // Getters & Setters

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getClientId() { return clientId; }

    public void setClientId(Long clientId) { this.clientId = clientId; }

    public Date getDate() { return date; }

    public void setDate(Date date) { this.date = date; }

    public String getObjectif() { return objectif; }

    public void setObjectif(String objectif) { this.objectif = objectif; }

    public String getHeureDebutVisite() { return heureDebutVisite; }

    public void setHeureDebutVisite(String heureDebutVisite) { this.heureDebutVisite = heureDebutVisite; }

    public String getHeureFinVisite() { return heureFinVisite; }

    public void setHeureFinVisite(String heureFinVisite) { this.heureFinVisite = heureFinVisite; }

    public String getActionsMiseEnPlace() { return actionsMiseEnPlace; }

    public void setActionsMiseEnPlace(String actionsMiseEnPlace) { this.actionsMiseEnPlace = actionsMiseEnPlace; }

    public StatutIntervention getStatut() { return statut; }

    public void setStatut(StatutIntervention statut) { this.statut = statut; }

    public TypeIntervention getTypeIntervention() { return typeIntervention; }

    public void setTypeIntervention(TypeIntervention typeIntervention) { this.typeIntervention = typeIntervention; }

    public MoyenIntervention getMoyenIntervention() { return moyenIntervention; }

    public void setMoyenIntervention(MoyenIntervention moyenIntervention) { this.moyenIntervention = moyenIntervention; }

    public CategorieIntervention getCategorieIntervention() { return categorieIntervention; }

    public void setCategorieIntervention(CategorieIntervention categorieIntervention) { this.categorieIntervention = categorieIntervention; }

    public NatureIntervention getNatureIntervention() { return natureIntervention; }

    public void setNatureIntervention(NatureIntervention natureIntervention) { this.natureIntervention = natureIntervention; }

    public boolean isAccesDev() { return accesDev; }

    public void setAccesDev(boolean accesDev) { this.accesDev = accesDev; }

    public boolean isFacturation() { return facturation; }

    public void setFacturation(boolean facturation) { this.facturation = facturation; }

    public String getObservationFacturation() { return observationFacturation; }

    public void setObservationFacturation(String observationFacturation) { this.observationFacturation = observationFacturation; }

    public List<UserResponse> getResponsable() { return responsable; }

    public void setResponsable(List<UserResponse> responsable) { this.responsable = responsable; }

    public List<UtilisateurInterventionResponse> getUtilisateurs() { return utilisateurs; }

    public void setUtilisateurs(List<UtilisateurInterventionResponse> utilisateurs) { this.utilisateurs = utilisateurs; }
}
