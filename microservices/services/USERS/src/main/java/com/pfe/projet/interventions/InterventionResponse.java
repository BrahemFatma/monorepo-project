package com.pfe.projet.interventions;



import lombok.*;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InterventionResponse {
    private Long id;
    private String client;
    private Date date;
    private String description;
    private String statut;
    private Set<Long> userIds;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public String toString() {
        return "InterventionResponse{" +
                "id=" + id +
                ", client='" + client + '\'' +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", statut='" + statut + '\'' +
                ", userIds=" + userIds +
                '}';
    }}
