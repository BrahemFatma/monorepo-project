package com.pfe.projet.Intervention;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserInterventionRepository extends JpaRepository<UserIntervention, Long> {
    List<UserIntervention> findByInterventionId(Long interventionId);
    List<UserIntervention> findByUserId(Long userId);
}