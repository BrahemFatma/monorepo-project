package com.pfe.projet.notifications;

// package com.pfe.projet.notifications.dto;

import java.util.Date;

public class ReunionNotificationDTO {
    private Long userId;
    private String userNom;
    private String titre;
    private String lieu;
    private String date;
    private String heure;
    private String description;
    private String type;

    private String descriptionAction;
    private Date dateAction;
    // Getter et Setter pour responsableActionId
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
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    // Getters et setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserNom() {
        return userNom;
    }


    public void setUserNom(String userNom) {
        this.userNom = userNom;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
