package com.pfe.projet.Routine;

import org.springframework.stereotype.Service;
import com.pfe.projet.user.UserClient;
import com.pfe.projet.user.UserResponse;

@Service
public class RoutineMapper {

    private final UserClient UserClient;

    public RoutineMapper(UserClient UserClient) {
        this.UserClient = UserClient;
    }

    public Routine toRoutine(RoutineRequest request) {
        if (request == null) {
            return null;
        }
        StatutRoutine statut = StatutRoutine.valueOf(request.statut());
        return new Routine(
                request.id(),
                request.nom(),
                request.description(),
                request.dateDebut(),
                request.dateFin(),
                request.userId(),
                request.clientId(),       // Nouveau champ
                request.projet(),         // Nouveau champ
                statut
        );
    }

    public RoutineResponse fromRoutine(Routine routine) {
        if (routine == null) {
            return null;
        }

        UserResponse user = (routine.getUserId() != null)
                ? UserClient.findUserById(routine.getUserId().toString()).orElse(null)
                : null;

        return new RoutineResponse(
                routine.getId(),
                routine.getNom(),
                routine.getDescription(),
                routine.getDateDebut(),
                routine.getDateFin(),
                routine.getStatut(),
                routine.getUserId(),
                user,
                routine.getClientId(),    // Nouveau champ
                routine.getProjet()       // Nouveau champ
        );
    }
}
