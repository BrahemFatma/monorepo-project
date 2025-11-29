package com.pfe.projet.Reunions;

public class ActionNotificationDTO {
    private Long responsableActionId;  // l'id du responsable d'action
    private String titreReunion;
    private String dateReunion;
    private String dateAction;
    private String descriptionAction;

    public Long getResponsableActionId() {
        return responsableActionId;
    }
    public void setResponsableActionId(Long responsableActionId) {
        this.responsableActionId = responsableActionId;
    }

    // Getter et Setter pour titreReunion
    public String getTitreReunion() {
        return titreReunion;
    }
    public void setTitreReunion(String titreReunion) {
        this.titreReunion = titreReunion;
    }

    // Getter et Setter pour dateReunion
    public String getDateReunion() {
        return dateReunion;
    }
    public void setDateReunion(String dateReunion) {
        this.dateReunion = dateReunion;
    }

    // Getter et Setter pour dateAction
    public String getDateAction() {
        return dateAction;
    }
    public void setDateAction(String dateAction) {
        this.dateAction = dateAction;
    }

    // Getter et Setter pour descriptionAction
    public String getDescriptionAction() {
        return descriptionAction;
    }
    public void setDescriptionAction(String descriptionAction) {
        this.descriptionAction = descriptionAction;
    }
}
