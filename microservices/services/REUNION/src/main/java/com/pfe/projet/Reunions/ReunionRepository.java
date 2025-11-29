package com.pfe.projet.Reunions;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReunionRepository extends JpaRepository<Reunion, Long>{
    List<Reunion> findByTitre(String titre);

    List<Reunion> findByDate(Date date);

    List<Reunion> findByLieu(String lieu);

    List<Reunion> findByDescriptionContaining(String keyword);

    @Query("SELECT r FROM Reunion r WHERE :userId MEMBER OF r.userIds")
    List<Reunion> findByUserId(Long userId);
    @Query("SELECT r FROM Reunion r JOIN r.userIds u WHERE u = :userId AND r.date = :date AND r.heure = :heure")
    List<Reunion> findByUserIdAndDateAndHeure(@Param("userId") Long userId, @Param("date") Date date, @Param("heure") String heure);
}

