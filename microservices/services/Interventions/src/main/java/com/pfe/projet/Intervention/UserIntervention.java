package com.pfe.projet.Intervention;

import jakarta.persistence.*;

@Entity
public class UserIntervention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long interventionId;

    public UserIntervention() {}

    public UserIntervention(Long userId, Long interventionId) {
        this.userId = userId;
        this.interventionId = interventionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getInterventionId() {
        return interventionId;
    }

    public void setInterventionId(Long interventionId) {
        this.interventionId = interventionId;
    }
}
