package com.pfe.projet.Routine;

import com.pfe.projet.exception.RoutineNotFoundException;
import com.pfe.projet.user.UserClient;
import com.pfe.projet.user.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class RoutineService {

    private final RoutineRepository repository;
    private final RoutineMapper mapper;
    private final UserClient userClient;

    @Autowired
    public RoutineService(RoutineRepository repository, RoutineMapper mapper, UserClient userClient) {
        this.repository = repository;
        this.mapper = mapper;
        this.userClient = userClient;
    }

    public Long createRoutine(RoutineRequest request) {
        var routine = repository.save(mapper.toRoutine(request));
        return routine.getId();
    }

    public void updateRoutine(Long id, RoutineRequest request) {
        var routine = repository.findById(id)
                .orElseThrow(() -> new RoutineNotFoundException(
                        format("Cannot update routine: No routine found with the provided ID :: %s", id)
                ));
        mergeRoutine(routine, request);
        repository.save(routine);
    }
    public List<UserResponse> loadResponsableForRoutine(List<Long> routineIds) {
        List<UserResponse> responsables = new ArrayList<>();

        for (Long routineId : routineIds) {
            // Récupérer la routine correspondante à l'ID fourni
            Optional<Routine> routineOptional = repository.findById(routineId);

            if (routineOptional.isPresent()) {
                Routine routine = routineOptional.get();
                Long userId = routine.getUserId();

                // Utiliser le client pour récupérer l'utilisateur par son ID
                Optional<UserResponse> userResponse = userClient.findUserById(userId.toString());

                // Ajouter l'utilisateur trouvé, ou null si non trouvé
                userResponse.ifPresent(responsables::add);
            }
        }

        return responsables;  // Retourne la liste des responsables trouvés
    }



    private void mergeRoutine(Routine routine, RoutineRequest request) {
        if (!request.nom().isBlank()) {
            routine.setNom(request.nom());
        }
        if (!request.description().isBlank()) {
            routine.setDescription(request.description());
        }
        if (request.dateDebut() != null) {
            routine.setDateDebut(request.dateDebut());
        }
        if (request.dateFin() != null) {
            routine.setDateFin(request.dateFin());
        }
        if (!request.statut().isBlank()) {
            routine.setStatut(StatutRoutine.valueOf(request.statut()));
        }
        if (request.userId() != null) {
            routine.setUserId(request.userId());
        }
        if (request.clientId() != null) {  // ✅ Nouveau champ
            routine.setClientId(request.clientId());
        }
        if (request.projet() != null && !request.projet().isBlank()) {  // ✅ Nouveau champ
            routine.setProjet(request.projet());
        }
    }

    public List<RoutineResponse> findAllRoutines() {
        return repository.findAll()
                .stream()
                .map(routine -> {
                    RoutineResponse response = mapper.fromRoutine(routine);
                    String userId = String.valueOf(routine.getUserId());
                    UserResponse user = userClient.findUserById(userId).orElse(null);
                    return new RoutineResponse(
                            routine.getId(),
                            routine.getNom(),
                            routine.getDescription(),
                            routine.getDateDebut(),
                            routine.getDateFin(),
                            routine.getStatut(),
                            routine.getUserId(),
                            user,
                            routine.getClientId(),   // ✅ Nouveau champ
                            routine.getProjet()

                    );
                })
                .collect(Collectors.toList());
    }

    public boolean existsById(Long routineId) {
        return repository.findById(routineId).isPresent();
    }

    public RoutineResponse findById(Long routineId) {
        return repository.findById(routineId)
                .map(routine -> {
                    RoutineResponse response = mapper.fromRoutine(routine);
                    String userId = String.valueOf(routine.getUserId());
                    UserResponse user = userClient.findUserById(userId).orElse(null);
                    return new RoutineResponse(
                            routine.getId(),
                            routine.getNom(),
                            routine.getDescription(),
                            routine.getDateDebut(),
                            routine.getDateFin(),
                            routine.getStatut(),
                            routine.getUserId(),
                            user,
                            routine.getClientId(),   // ✅ Nouveau champ
                            routine.getProjet()
                    );
                })
                .orElseThrow(() -> new RoutineNotFoundException(
                        format("No routine found with the provided ID:: %s", routineId)
                ));
    }

    public void deleteRoutine(Long routineId) {
        repository.deleteById(routineId);
    }
}
