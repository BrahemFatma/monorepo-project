package com.pfe.projet.User;
import com.pfe.projet.exception.UserNotFoundException;
import com.pfe.projet.interventions.*;
import com.pfe.projet.notifications.NotificationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/Users")

public class UserController {
    @Autowired
    private CsvExportService csvExportService;
    @Autowired
    private UserInterventionService userInterventionService;
    private final UserService service;
    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }
    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportRoutinesToCsv() throws IOException {
        byte[] csvContent = csvExportService.exportUsersToCsv();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.csv")
                .header(HttpHeaders.CONTENT_TYPE, "text/csv")
                .body(csvContent);
    }
    @GetMapping("/{userId}/notifications")
    public ResponseEntity<List<NotificationResponse>> getUserNotifications(@PathVariable Long userId) {
        String notificationServiceUrl = "http://localhost:8050/api/v1/Notifications/user/" + userId;

        ResponseEntity<NotificationResponse[]> response = restTemplate.getForEntity(notificationServiceUrl, NotificationResponse[].class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            List<NotificationResponse> notifications = Arrays.asList(response.getBody());

            UserResponse userResponse = service.findById(userId);
            if (userResponse == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            List<NotificationResponse> userNotificationResponses = notifications.stream()
                    .map(notification -> new NotificationResponse(
                            notification.id(),
                            notification.message(),
                            notification.date()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(userNotificationResponses);
        } else {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @GetMapping("/short-info")
    public ResponseEntity<List<UserShortInfo>> getAllUsersShortInfo() {
        return ResponseEntity.ok(service.findAllUsersShortInfo());
    }


    @PostMapping
    public ResponseEntity<Long> createUser(
            @RequestBody @Valid UserRequest request

    ) {
        return ResponseEntity.ok(service.createUser(request));
    }

    @PutMapping
    public ResponseEntity<Void> updateUser(
            @RequestBody @Valid UserRequest request
    ) {
        service.updateUser(request);
        return ResponseEntity.accepted().build();
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        List<UserResponse> users = service.findAllUsers().stream()
                .map(user -> {
                    if (user.getReunionId() == null || user.getReunionId().equals("DEFAULT_REUNION_ID") || user.getReunionId().equals("NULL")) {
                        return user.withReunionId("Aucune réunion assignée");
                    }
                    return user;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    @GetMapping("/exist/{user-id}")
    public ResponseEntity<Boolean> existsById(
            @PathVariable("user-id") Long userId
    ) {
        return ResponseEntity.ok(service.existisById(userId));
    }

    @GetMapping("/{user-id}")
    public ResponseEntity<UserResponse> findById(
            @PathVariable("user-id") Long userId
    ) {

        UserResponse userResponse = service.findById(userId);

        if (userResponse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (userResponse.getReunionId() == null || userResponse.getReunionId().equals("DEFAULT_REUNION_ID")) {
            userResponse = userResponse.withReunionId("Aucune réunion assignée");
        }

        return ResponseEntity.ok(userResponse);
    }



    @GetMapping("/{userId}/interventions")
    public ResponseEntity<List<UserInterventionResponse>> getUserInterventions(@PathVariable Long userId) {
        String interventionServiceUrl = "http://localhost:8030/api/v1/Interventions/user/" + userId;
        ResponseEntity<InterventionResponse[]> response = restTemplate.getForEntity(interventionServiceUrl, InterventionResponse[].class);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            List<InterventionResponse> interventions = Arrays.asList(response.getBody());
            UserResponse userResponse = service.findById(userId);
            if (userResponse == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            List<UserInterventionResponse> userInterventionResponses = interventions.stream()
                    .map(intervention -> new UserInterventionResponse(
                            userResponse.getId(),
                            userResponse.getNom(),
                            userResponse.getPrenom(),
                            userResponse.getEmail(),
                            intervention.getId(),
                            intervention.getClient(),
                            intervention.getDescription()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(userInterventionResponses);
        } else {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }




    @DeleteMapping ("/{user-id}")
    public ResponseEntity<Void> delete (
            @PathVariable("user-id") Long userId
    ){
        service.deleteUser(userId);
        return ResponseEntity.accepted().build();
    }
    @GetMapping("/reunion/{reunionId}/participants")
    public ResponseEntity<List<UserResponse>> getParticipantsByReunionId(@PathVariable String reunionId) {
        List<UserResponse> participants = service.findParticipantsByReunionId(reunionId);
        if (participants.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(participants);
    }
    @GetMapping("/without-reunion")
    public ResponseEntity<List<UserResponse>> findAllWithoutReunion() {
        List<UserResponse> usersWithoutReunion = service.findUsersWithoutReunion();
        return ResponseEntity.ok(usersWithoutReunion);
    }


    @GetMapping("/without-reunion/{user-id}")
    public ResponseEntity<UserResponse> findByIdWithoutReunion(@PathVariable("user-id") Long userId) {
        UserResponse userResponse = service.findById(userId);

        if (userResponse == null || userResponse.getReunionId() != null && !userResponse.getReunionId().equals("DEFAULT_REUNION_ID") && !userResponse.getReunionId().equals("NULL")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(userResponse);
    }







}
