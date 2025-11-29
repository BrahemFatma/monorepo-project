package com.pfe.projet.User;

import com.pfe.projet.exception.UserNotFoundException;
import com.pfe.projet.notifications.NotificationClient;
import com.pfe.projet.notifications.NotificationResponse;
import com.pfe.projet.reunion.ReunionResponse;
import com.pfe.projet.reunion.reunionclient;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final reunionclient reunionclient;
    @Autowired
    private NotificationClient notificationClient;
    @Autowired
    public UserService(UserRepository repository, UserMapper mapper, reunionclient routineclient) {
        this.repository = repository;
        this.mapper = mapper;
        this.reunionclient = routineclient;
    }
    public List<NotificationResponse> findNotificationsByUserId(Long userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Long> notificationIds = user.getNotificationIds();

        if (notificationIds == null || notificationIds.isEmpty()) {
            return new ArrayList<>();
        }

        return notificationIds.stream()
                .map(notificationId -> {
                    NotificationResponse notification = notificationClient.getNotificationById(notificationId).orElse(null);

                    if (notification == null) {
                        notification = new NotificationResponse(
                                notificationId,
                                "Message not found",
                                "Unknown Date"
                        );
                    }

                    return new NotificationResponse(
                            notification.id(),
                            notification.message(),
                            notification.date()
                    );
                })
                .collect(Collectors.toList());
    }

    public Long createUser(UserRequest request){
        var User = repository.save(mapper.toUser(request));
        return User.getId();
    }

    public void updateUser(UserRequest request) {
        var User = repository.findById(request.id())
                .orElseThrow(() -> new UserNotFoundException(
                        format("cannot update User:: No User found with the provided ID :: %s", request.id())
                ));
        mergerUser(User, request);
        repository.save(User);
    }

    private void mergerUser(User user, UserRequest request) {
        if (StringUtils.isNotBlank(request.nom())) {
            user.setNom(request.nom());
        }
        if (StringUtils.isNotBlank(request.prenom())) {
            user.setPrenom(request.prenom());
        }
        if (StringUtils.isNotBlank(request.motDePasse())) {
            user.setMotDePasse(request.motDePasse());
        }
        if (StringUtils.isNotBlank(request.email())) {
            user.setEmail(request.email());
        }
    }
    public List<UserShortInfo> findAllUsersShortInfo() {
        return repository.findAll()
                .stream()
                .map(user -> new UserShortInfo(user.getId(), user.getNom(), user.getPrenom()))
                .collect(Collectors.toList());
    }


    public List<UserResponse> findAllUsers() {
        return repository.findAll()
                .stream()
                .map(user -> {
                    String reunionId = user.getReunionId();
                    var reunion = (reunionId != null && !reunionId.equals("DEFAULT_REUNION_ID") && !reunionId.equals("NULL"))
                            ? reunionclient.findReunionById(reunionId).orElse(null)
                            : null;

                    if (reunion == null) {
                        reunion = new ReunionResponse(
                                "Aucune réunion assignée",
                                "Aucune réunion assignée",
                                "",
                                null,
                                "",
                                ""
                        );
                    }

                    return new UserResponse(
                            user.getId(),
                            user.getNom(),
                            user.getPrenom(),
                            user.getEmail(),
                            user.getMotDePasse(),
                            user.getRole().toString(),
                            user.getReunionId(),
                            reunion
                    );
                })
                .collect(Collectors.toList());
    }

    public boolean existisById(Long userId) {
        return repository.findById(userId).isPresent();
    }

    public UserResponse findById(Long userId) {
        var user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        format("No user found with the provided ID: %s", userId)
                ));


        String reunionId = user.getReunionId();
        var reunion = (reunionId != null && !reunionId.equals("DEFAULT_REUNION_ID") && !reunionId.equals("NULL"))
                ? reunionclient.findReunionById(reunionId).orElse(null)
                : null;


        if (reunion == null) {
            reunion = new ReunionResponse(
                    "Aucune réunion assignée",
                    "Aucune réunion assignée",
                    "",
                    null,
                    "",
                    ""
            );
        }

        return new UserResponse(
                user.getId(),
                user.getNom(),
                user.getPrenom(),
                user.getEmail(),
                user.getMotDePasse(),
                user.getRole().toString(),
                user.getReunionId(),
                reunion
        );
    }

    public void deleteUser(Long userId) {
        repository.deleteById(userId);
    }
    public List<UserResponse> findParticipantsByReunionId(String reunionId) {
        List<User> users = repository.findByReunionId(reunionId);

        return users.stream()
                .map(user -> {
                    var reunion = reunionclient.findReunionById(reunionId).orElse(null);

                    if (reunion == null) {
                        reunion = new ReunionResponse(
                                "Aucune réunion assignée",
                                "Aucune réunion assignée",
                                "",
                                null,
                                "",
                                ""
                        );
                    }

                    return new UserResponse(
                            user.getId(),
                            user.getNom(),
                            user.getPrenom(),
                            user.getEmail(),
                            user.getMotDePasse(),
                            user.getRole().toString(),
                            user.getReunionId(),
                            reunion
                    );
                })
                .collect(Collectors.toList());
    }

    public List<UserResponse> findUsersWithoutReunion() {
        return repository.findAll()
                .stream()
                .filter(user -> user.getReunionId() == null ||
                        user.getReunionId().equals("DEFAULT_REUNION_ID") ||
                        user.getReunionId().equals("NULL"))
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getNom(),
                        user.getPrenom(),
                        user.getEmail(),
                        user.getMotDePasse(),
                        user.getRole().toString()
                ))
                .collect(Collectors.toList());
    }
    public UserResponse findByNom(String nom) {
        return repository.findByNom(nom)
                .map(mapper::fromUser)
                .orElse(null);
    }



}
