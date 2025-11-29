package com.pfe.projet.Intervention;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.MultiValueMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import jakarta.validation.Valid;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/v1/Interventions")
public class InterventionController {

    private final InterventionService service;
    @Autowired
    private UserInterventionService userInterventionService;
    @Autowired
    private InterventionRepository repository;
    @Autowired
    private InterventionMapper mapper;
    @Autowired
    private CsvExportService csvExportService;
    @Autowired
    private ClientServiceClient clientServiceClient;

    @Autowired
    public InterventionController(InterventionService service) {
        this.service = service;
    }
    @PostMapping("/{interventionId}/users/{userId}")
    public ResponseEntity<Void> addUserToIntervention(@PathVariable Long userId, @PathVariable Long interventionId) {
        userInterventionService.addUserToIntervention(userId, interventionId);
        return ResponseEntity.ok().build();
    }
    @PostMapping
    public ResponseEntity<Long> createIntervention(
            @RequestBody @Valid InterventionRequest request
    ) {
        return ResponseEntity.ok(service.createIntervention(request));
    }


    @GetMapping("/export-interventions/csv")
    public ResponseEntity<byte[]> exportInterventionsToCsv() throws IOException {
        byte[] csvData = csvExportService.exportInterventionsToCsv();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=interventions.csv")
                .header(HttpHeaders.CONTENT_TYPE, "text/csv")
                .body(csvData);
    }
    @PutMapping("/{interventionId}")
    public ResponseEntity<Void> updateIntervention(
            @PathVariable Long interventionId,
            @RequestBody @Valid InterventionRequest request
    ) {
        InterventionRequest updatedRequest = new InterventionRequest(
                interventionId,
                request.clientId(),
                request.date(),
                request.objectif(),
                request.statut(),
                request.heureDebutVisite(),
                request.heureFinVisite(),
                request.actionsMiseEnPlace(),
                request.userId(),
                request.utilisateurs(),
                request.typeIntervention(),
                request.moyenIntervention(),
                request.categorieIntervention(),
                request.natureIntervention(),
                request.accesDev(),
                request.facturation(),
                request.observationFacturation()
        );

        service.updateIntervention(updatedRequest);
        return ResponseEntity.accepted().build();
    }


    @GetMapping
    public ResponseEntity<List<InterventionResponse>> findAll() {
        return ResponseEntity.ok(service.findAllInterventions());
    }

    @GetMapping("/exist/{intervention-id}")
    public ResponseEntity<Boolean> existsById(
            @PathVariable("intervention-id") Long interventionId
    ) {
        return ResponseEntity.ok(service.existsById(interventionId));
    }

    @GetMapping("/{intervention-id}")
    public ResponseEntity<InterventionResponse> findById(
            @PathVariable("intervention-id") Long interventionId) {
        InterventionResponse intervention = service.findById(interventionId);

        if (intervention == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(intervention);
    }


    @DeleteMapping("/{intervention-id}")
    public ResponseEntity<Void> delete(
            @PathVariable("intervention-id") Long interventionId
    ) {
        service.deleteIntervention(interventionId);
        return ResponseEntity.accepted().build();
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<InterventionResponse>> getInterventionsByUserId(@PathVariable Long userId) {
        List<UserIntervention> userInterventions = repository.findByUserId(userId);

        List<InterventionResponse> interventions = userInterventions.stream()
                .map(userIntervention -> service.findById(userIntervention.getInterventionId()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(interventions);
    }
    @GetMapping("/{intervention-id}/users")
    public ResponseEntity<List<UserResponse>> getUsersByIntervention(@PathVariable("intervention-id") Long interventionId) {
        List<UserResponse> users = service.getUsersByIntervention(interventionId);

        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(users);
    }
    @GetMapping("/generateReport/{id}")
    public ResponseEntity<byte[]> generateReport(@PathVariable Long id) {
        Optional<Interventions> optionalIntervention = repository.findById(id);

        if (optionalIntervention.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Interventions intervention = optionalIntervention.get();
        ClientDTO client = clientServiceClient.getClientById(intervention.getClientId());
        String clientName = (client != null) ? client.getNom() : "Nom du client non disponible";

        List<UserResponse> intervenants = userInterventionService.getUsersForIntervention(id);
        String intervenantsString = intervenants.isEmpty() ?
                "Aucun intervenant disponible" :
                intervenants.stream()
                        .map(i -> i.getPrenom() + " " + i.getNom())
                        .collect(Collectors.joining(", "));

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        String formattedDate = sdf.format(intervention.getDate());

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("Client", clientName);
        parameters.put("DateVisite", formattedDate);
        parameters.put("HeureDebut", intervention.getHeureDebutVisite());
        parameters.put("HeureFin", intervention.getHeureFinVisite());
        parameters.put("Intervenant", intervenantsString);
        parameters.put("Objectifs", formatWithDashes(intervention.getObjectif()));
        parameters.put("Actions", formatWithDashes(intervention.getActionsMiseEnPlace()));

        // ✅ Nouveaux attributs
        parameters.put("Statut", intervention.getStatut() != null ? intervention.getStatut().toString() : "Non défini");
        parameters.put("Type", intervention.getTypeIntervention() != null ? intervention.getTypeIntervention().toString() : "Non défini");
        parameters.put("Moyen", intervention.getMoyenIntervention() != null ? intervention.getMoyenIntervention().toString() : "Non défini");
        parameters.put("Categorie", intervention.getCategorieIntervention() != null ? intervention.getCategorieIntervention().toString() : "Non défini");
        parameters.put("Nature", intervention.getNatureIntervention() != null ? intervention.getNatureIntervention().toString() : "Non défini");
        parameters.put("AccesDev", intervention.isAccesDev() ? "Oui" : "Non");
        parameters.put("Facturation", intervention.isFacturation() ? "Oui" : "Non");
        parameters.put("ObservationFacturation", intervention.getObservationFacturation() != null ? intervention.getObservationFacturation() : "Aucune");

        // Logo
        parameters.put("LOGO_PATH", "C:\\Users\\fatou\\Desktop\\microservices\\services\\Interventions\\src\\main\\resources\\templates\\logo.png");

        // Utilisateurs
        List<Map<String, Object>> usersData = intervention.getUtilisateurs().stream().map(ui -> {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("Utilisateur", ui.getNomUtilisateur());
            userMap.put("Service", ui.getService());
            userMap.put("Points", String.join(", ", ui.getPointsADiscuter()));
            return userMap;
        }).collect(Collectors.toList());

        if (usersData.isEmpty()) {
            usersData.add(Collections.singletonMap("Utilisateur", "Aucun utilisateur assigné"));
        }

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(usersData);

        try {
            String filePath = "C:\\Users\\fatou\\Desktop\\microservices\\services\\Interventions\\src\\main\\resources\\templates\\pv.jrxml";
            JasperReport report = JasperCompileManager.compileReport(filePath);
            JasperPrint print = JasperFillManager.fillReport(report, parameters, dataSource);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(print, byteArrayOutputStream);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=fiche_intervention_" + id + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(byteArrayOutputStream.toByteArray());

        } catch (JRException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    private String formatWithDashes(String input) {
        if (input == null || input.isBlank()) return "";
        return Arrays.stream(input.split("\n"))
                .map(line -> "- " + line.trim())
                .collect(Collectors.joining("\n"));
    }
    @GetMapping("/client/{clientId}")
    public List<Interventions> getInterventionsByClientId(@PathVariable Long clientId) {
        return service.getInterventionsByClientId(clientId);
    }
    @PostMapping("/{interventionId}/transcribe")
    public ResponseEntity<String> updateInterventionFromAudio(
            @PathVariable Long interventionId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String type
    ) {
        try {

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            File convFile = convert(file);

            body.add("audio", new FileSystemResource(convFile));

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:5001/transcribe", requestEntity, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(response.getBody());
            String plainText = node.get("text").asText();
            String transcription = response.getBody();

            // 2. Mettre à jour l’intervention dans la base de données
            // mise à jour de l'entité
            Interventions intervention = repository.findById(interventionId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

            if ("objectif".equalsIgnoreCase(type)) {
                intervention.setObjectif(plainText);
            } else if ("actions".equalsIgnoreCase(type)) {
                intervention.setActionsMiseEnPlace(plainText);
            } else {
                return ResponseEntity.badRequest().body("Type invalide. Utiliser 'objectif' ou 'actions'");
            }

            repository.save(intervention);
            return ResponseEntity.ok( plainText);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors du traitement audio");
        }
    }
    @PostMapping(value = "/create-from-audio", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> createInterventionFromAudio(
            @RequestParam(value = "objectifFile", required = false) MultipartFile objectifFile,
            @RequestParam(value = "actionsFile", required = false) MultipartFile actionsFile,
            @RequestParam("clientId") Long clientId,
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam("heureDebut") String heureDebut,
            @RequestParam("heureFin") String heureFin,
            @RequestParam("userId") Long userId,

            // Nouveaux attributs
            @RequestParam("typeIntervention") TypeIntervention typeIntervention,
            @RequestParam("moyenIntervention") MoyenIntervention moyenIntervention,
            @RequestParam("categorieIntervention") CategorieIntervention categorieIntervention,
            @RequestParam("natureIntervention") NatureIntervention natureIntervention,
            @RequestParam("accesDev") boolean accesDev,
            @RequestParam("facturation") boolean facturation,
            @RequestParam(value = "observationFacturation", required = false) String observationFacturation
            // Note : utilisateurs sera null ici car tu ne peux pas le passer via un formulaire multipart classique facilement
    ) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            String objectif = null;
            String actions = null;

            if (objectifFile != null && !objectifFile.isEmpty()) {
                objectif = transcrireAudio(objectifFile, restTemplate);
            }

            if (actionsFile != null && !actionsFile.isEmpty()) {
                actions = transcrireAudio(actionsFile, restTemplate);
            }

            InterventionRequest interventionRequest = new InterventionRequest(
                    null,
                    clientId,
                    date,
                    objectif,
                    StatutIntervention.EN_ATTENTE,
                    heureDebut,
                    heureFin,
                    actions,
                    userId,
                    null, // utilisateurs (optionnel ici)
                    typeIntervention,
                    moyenIntervention,
                    categorieIntervention,
                    natureIntervention,
                    accesDev,
                    facturation,
                    observationFacturation
            );

            Long id = service.createIntervention(interventionRequest);
            return ResponseEntity.ok(id);

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur traitement audio", e);
        }
    }

    private String transcrireAudio(MultipartFile file, RestTemplate restTemplate) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        File convFile = convert(file);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("audio", new FileSystemResource(convFile));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:5001/transcribe", requestEntity, String.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur de transcription");
        }

        return response.getBody();
    }
    @PostMapping("/transcribe")
    public ResponseEntity<String> transcribeAudio(@RequestParam("file") MultipartFile file) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // Préparer les headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // Convertir MultipartFile en File
            File convFile = convert(file);

            // Construire la requête multipart
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("audio", new FileSystemResource(convFile)); // clé "audio" attendue par ton backend Python

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // URL de l'API Python
            String pythonApiUrl = "http://localhost:5001/transcribe";

            // Appel HTTP vers l’API Python
            ResponseEntity<String> response = restTemplate.postForEntity(pythonApiUrl, requestEntity, String.class);

            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la transcription : " + e.getMessage());
        }
    }

    // Méthode utilitaire pour convertir MultipartFile en File
    private File convert(MultipartFile file) throws IOException {
        File convFile = File.createTempFile("audio", ".wav");
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }



}