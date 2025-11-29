package com.pfe.projet.Intervention;


import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterventionRepository extends JpaRepository<Interventions, Long> {
    List<Interventions> findByStatut(StatutIntervention statut);
    @Query("SELECT ui FROM UserIntervention ui WHERE ui.userId = :userId")
    List<UserIntervention> findByUserId(@Param("userId") Long userId);
    List<Interventions> findByClientId(Long clientId);  // Exemple de méthode pour récupérer les interventions d'un client


}