package com.pfe.projet.Reunions;

import jakarta.persistence.*;
import lombok.Builder;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Builder
public class Reunion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String description;
    private Date date;
    private String heure;
    private String lieu;
    private String pv;

    @Enumerated(EnumType.STRING)
    private TypeReunion type;

    private Long clientId;
    private Date dateFin;
    private String heureFin;
    private String dureePrevue;
    private String dureeReelle;

    private Boolean actionMiseEnPlace;
    private String descriptionAction;
    private Date dateAction;
    private Long responsableActionId;
    private Boolean clotureAction;
    private Date dateCloture;

    @ElementCollection
    @CollectionTable(name = "reunion_users", joinColumns = @JoinColumn(name = "reunion_id"))
    @Column(name = "user_id")
    private List<Long> userIds;

    @Lob
    private String documents;

    public Reunion() {}

    public Reunion(Long id, String titre, String description, Date date, String heure, String lieu, String pv, String documents,
                   List<Long> userIds, TypeReunion type, Long clientId, Date dateFin, String heureFin, String dureePrevue,
                   String dureeReelle, Boolean actionMiseEnPlace, String descriptionAction, Date dateAction,
                   Long responsableActionId, Boolean clotureAction, Date dateCloture) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.date = date;
        this.heure = heure;
        this.lieu = lieu;
        this.pv = pv;
        this.documents = documents;
        this.userIds = userIds;
        this.type = type;
        this.clientId = clientId;
        this.dateFin = dateFin;
        this.heureFin = heureFin;
        this.dureePrevue = dureePrevue;
        this.dureeReelle = dureeReelle;
        this.actionMiseEnPlace = actionMiseEnPlace;
        this.descriptionAction = descriptionAction;
        this.dateAction = dateAction;
        this.responsableActionId = responsableActionId;
        this.clotureAction = clotureAction;
        this.dateCloture = dateCloture;
    }

    public Reunion(ReunionRequest request) {
        this.titre = request.titre();
        this.description = request.description();
        this.date = request.date();
        this.heure = request.heure();
        this.lieu = request.lieu();
        this.pv = request.pv();
        this.documents = request.documents();
        this.userIds = request.userIds();
        this.type = request.type();
        this.clientId = request.clientId();
        this.dateFin = request.dateFin();
        this.heureFin = request.heureFin();
        this.dureePrevue = request.dureePrevue();
        this.dureeReelle = request.dureeReelle();
        this.actionMiseEnPlace = request.actionMiseEnPlace();
        this.descriptionAction = request.descriptionAction();
        this.dateAction = request.dateAction();
        this.responsableActionId = request.responsableActionId();
        this.clotureAction = request.clotureAction();
        this.dateCloture = request.dateCloture();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getPv() {
        return pv;
    }

    public void setPv(String pv) {
        this.pv = pv;
    }

    public TypeReunion getType() {
        return type;
    }

    public void setType(TypeReunion type) {
        this.type = type;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(String heureFin) {
        this.heureFin = heureFin;
    }

    public String getDureePrevue() {
        return dureePrevue;
    }

    public void setDureePrevue(String dureePrevue) {
        this.dureePrevue = dureePrevue;
    }

    public String getDureeReelle() {
        return dureeReelle;
    }

    public void setDureeReelle(String dureeReelle) {
        this.dureeReelle = dureeReelle;
    }

    public Boolean getActionMiseEnPlace() {
        return actionMiseEnPlace;
    }

    public void setActionMiseEnPlace(Boolean actionMiseEnPlace) {
        this.actionMiseEnPlace = actionMiseEnPlace;
    }

    public String getDescriptionAction() {
        return descriptionAction;
    }

    public void setDescriptionAction(String descriptionAction) {
        this.descriptionAction = descriptionAction;
    }

    public Date getDateAction() {
        return dateAction;
    }

    public void setDateAction(Date dateAction) {
        this.dateAction = dateAction;
    }

    public Long getResponsableActionId() {
        return responsableActionId;
    }

    public void setResponsableActionId(Long responsableActionId) {
        this.responsableActionId = responsableActionId;
    }

    public Boolean getClotureAction() {
        return clotureAction;
    }

    public void setClotureAction(Boolean clotureAction) {
        this.clotureAction = clotureAction;
    }

    public Date getDateCloture() {
        return dateCloture;
    }

    public void setDateCloture(Date dateCloture) {
        this.dateCloture = dateCloture;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public String getDocuments() {
        return documents;
    }

    public void setDocuments(String documents) {
        this.documents = documents;
    }

    @Override
    public String toString() {
        return "Reunion{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", heure='" + heure + '\'' +
                ", lieu='" + lieu + '\'' +
                ", type=" + type +
                ", clientId=" + clientId +
                ", dateFin=" + dateFin +
                ", heureFin='" + heureFin + '\'' +
                ", dureePrevue='" + dureePrevue + '\'' +
                ", dureeReelle='" + dureeReelle + '\'' +
                ", actionMiseEnPlace=" + actionMiseEnPlace +
                ", descriptionAction='" + descriptionAction + '\'' +
                ", dateAction=" + dateAction +
                ", responsableActionId=" + responsableActionId +
                ", clotureAction=" + clotureAction +
                ", dateCloture=" + dateCloture +
                '}';
    }

    public void generatePv() {
        StringBuilder sb = new StringBuilder();
        sb.append("Procès-Verbal de la Réunion : ").append(titre).append("\n");
        sb.append("Date : ").append(date).append("\n");
        sb.append("Heure : ").append(heure).append("\n");
        sb.append("Lieu : ").append(lieu).append("\n");
        sb.append("Participants : ").append(userIds.stream().map(String::valueOf).collect(Collectors.joining(", "))).append("\n");
        sb.append("Description : ").append(description).append("\n");
        sb.append("Documents associés : ").append(documents).append("\n");

        this.pv = sb.toString();
    }
}
