package com.pfe.projet.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    List<User> findByReunionId(String reunionId);
    Optional<User> findByNom(String nom);  // <-- méthode ajoutée ici


}
