package com.pfe.projet.Intervention;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Interventions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clientId;  // Utilisation d'un clientId au lieu d'un objet Client
    private Date date;
    @Column(length = 1000)
    private String objectif;
    private String heureDebutVisite;
    private String heureFinVisite;
    @Column(length = 10000)

    private String actionsMiseEnPlace;

    @Enumerated(EnumType.STRING)
    private StatutIntervention statut;
    @Enumerated(EnumType.STRING)
    private TypeIntervention typeIntervention;

    @Enumerated(EnumType.STRING)
    private MoyenIntervention moyenIntervention;

    @Enumerated(EnumType.STRING)
    private CategorieIntervention categorieIntervention;

    @Enumerated(EnumType.STRING)
    private NatureIntervention natureIntervention;

    private boolean accesDev;
    private boolean facturation;

    @Column(length = 1000)
    private String observationFacturation;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "intervention_id") // Cela ajoute une colonne dans UtilisateurIntervention
    private List<UtilisateurIntervention> utilisateurs;
    public Interventions() {
    }

    public Interventions(Long id, Long clientId, Date date, String objectif, String heureDebutVisite, String heureFinVisite,
                         String actionsMiseEnPlace, StatutIntervention statut, TypeIntervention typeIntervention,
                         MoyenIntervention moyenIntervention, CategorieIntervention categorieIntervention,
                         NatureIntervention natureIntervention, boolean accesDev, boolean facturation,
                         String observationFacturation, List<UtilisateurIntervention> utilisateurs) {
        this.id = id;
        this.clientId = clientId;
        this.date = date;
        this.objectif = objectif;
        this.heureDebutVisite = heureDebutVisite;
        this.heureFinVisite = heureFinVisite;
        this.actionsMiseEnPlace = actionsMiseEnPlace;
        this.statut = statut;
        this.typeIntervention = typeIntervention;
        this.moyenIntervention = moyenIntervention;
        this.categorieIntervention = categorieIntervention;
        this.natureIntervention = natureIntervention;
        this.accesDev = accesDev;
        this.facturation = facturation;
        this.observationFacturation = observationFacturation;
        this.utilisateurs = utilisateurs;
    }

    public TypeIntervention getTypeIntervention() {
        return typeIntervention;
    }

    public void setTypeIntervention(TypeIntervention typeIntervention) {
        this.typeIntervention = typeIntervention;
    }

    public MoyenIntervention getMoyenIntervention() {
        return moyenIntervention;
    }

    public void setMoyenIntervention(MoyenIntervention moyenIntervention) {
        this.moyenIntervention = moyenIntervention;
    }

    public CategorieIntervention getCategorieIntervention() {
        return categorieIntervention;
    }

    public void setCategorieIntervention(CategorieIntervention categorieIntervention) {
        this.categorieIntervention = categorieIntervention;
    }

    public NatureIntervention getNatureIntervention() {
        return natureIntervention;
    }

    public void setNatureIntervention(NatureIntervention natureIntervention) {
        this.natureIntervention = natureIntervention;
    }

    public boolean isAccesDev() {
        return accesDev;
    }

    public void setAccesDev(boolean accesDev) {
        this.accesDev = accesDev;
    }

    public boolean isFacturation() {
        return facturation;
    }

    public void setFacturation(boolean facturation) {
        this.facturation = facturation;
    }

    public String getObservationFacturation() {
        return observationFacturation;
    }

    public void setObservationFacturation(String observationFacturation) {
        this.observationFacturation = observationFacturation;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getObjectif() {
        return objectif;
    }

    public void setObjectif(String objectif) {
        this.objectif = objectif;
    }

    public String getHeureDebutVisite() {
        return heureDebutVisite;
    }

    public void setHeureDebutVisite(String heureDebutVisite) {
        this.heureDebutVisite = heureDebutVisite;
    }

    public String getHeureFinVisite() {
        return heureFinVisite;
    }

    public void setHeureFinVisite(String heureFinVisite) {
        this.heureFinVisite = heureFinVisite;
    }

    public String getActionsMiseEnPlace() {
        return actionsMiseEnPlace;
    }

    public void setActionsMiseEnPlace(String actionsMiseEnPlace) {
        this.actionsMiseEnPlace = actionsMiseEnPlace;
    }

    public StatutIntervention getStatut() {
        return statut;
    }

    public void setStatut(StatutIntervention statut) {
        this.statut = statut;
    }

    public List<UtilisateurIntervention> getUtilisateurs() {
        return utilisateurs;
    }

    public void setUtilisateurs(List<UtilisateurIntervention> utilisateurs) {
        this.utilisateurs = utilisateurs;
    }

    @Override
    public String toString() {
        return "Interventions{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", date=" + date +
                ", objectif='" + objectif + '\'' +
                ", heureDebutVisite='" + heureDebutVisite + '\'' +
                ", heureFinVisite='" + heureFinVisite + '\'' +
                ", actionsMiseEnPlace='" + actionsMiseEnPlace + '\'' +
                ", statut=" + statut +
                ", typeIntervention=" + typeIntervention +
                ", moyenIntervention=" + moyenIntervention +
                ", categorieIntervention=" + categorieIntervention +
                ", natureIntervention=" + natureIntervention +
                ", accesDev=" + accesDev +
                ", facturation=" + facturation +
                ", observationFacturation='" + observationFacturation + '\'' +
                '}';
    }
}
