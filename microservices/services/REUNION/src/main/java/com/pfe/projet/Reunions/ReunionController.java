package com.pfe.projet.Reunions;
import com.opencsv.CSVWriter;
import com.pfe.projet.ReunionApplication;
import jakarta.validation.Valid;
import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController

@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/v1/Reunions")
public class ReunionController {
    @Autowired
    private UserServiceClient userServiceClient;
    private final KafkaProducerService kafkaProducerService;
    @Autowired
    private CsvExportService csvExportService;
    private final ReunionService service;

    @Autowired
    private ReunionRepository repository;
    private final UserService userService;
    private final DocumentService documentService;


    @Autowired
    private ReunionRepository reunionRepository;

    @Autowired
    public ReunionController(KafkaProducerService kafkaProducerService, ReunionService service, UserService userService, DocumentService documentService) {
        this.service = service;
        this.userService = userService;
        this.documentService = documentService;
        this.kafkaProducerService = kafkaProducerService;
    }
    @GetMapping("/user/{userId}/reunion")
    public ResponseEntity<List<Reunion>> getReunionForUser(@PathVariable Long userId) {
        List<Reunion> reunions = service.findReunionByUserId(userId);

        if (!reunions.isEmpty()) {
            return ResponseEntity.ok(reunions);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }


    @PostMapping("/create")
    public ResponseEntity<Long> createReunion(@RequestBody @Valid ReunionRequest request) {
        try {
            Long reunionId = service.createReunion(request);
            System.out.println("Réunion créée avec l'ID : " + reunionId);
            return ResponseEntity.ok(reunionId);
        } catch (Exception e) {
            System.out.println("Erreur lors de la création de la réunion: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/gg")
    public String hello(){
        return ("jeml");
    }


    @GetMapping("/export-reunions/csv")
    public ResponseEntity<byte[]> exportReunionsToCsv() throws IOException {
        // Récupération des réunions
        List<Reunion> reunions = csvExportService.getReunionsList();

        // Création du CSV
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter);

        // En-têtes de colonnes
        csvWriter.writeNext(new String[]{
                "Titre", "Date", "Description", "Lieu", "Heure",
                "PV", "Documents", "User IDs", "Type", "Client ID",
                "Date Fin", "Heure Fin", "Durée Prévue", "Durée Réelle",
                "Action Mise en Place", "Date Action", "Responsable Action ID",
                "Clôture Action", "Date Clôture"
        });

        // Formatage des dates
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (Reunion reunion : reunions) {
            csvWriter.writeNext(new String[]{
                    reunion.getTitre(),
                    reunion.getDate() != null ? dateFormat.format(reunion.getDate()) : "",
                    reunion.getDescription(),
                    reunion.getLieu(),
                    reunion.getHeure(),
                    reunion.getPv(),
                    reunion.getDocuments(),
                    reunion.getUserIds() != null ? reunion.getUserIds().toString() : "",
                    reunion.getType() != null ? reunion.getType().toString() : "", // conversion TypeReunion → String
                    reunion.getClientId() != null ? reunion.getClientId().toString() : "",
                    reunion.getDateFin() != null ? dateFormat.format(reunion.getDateFin()) : "",
                    reunion.getHeureFin(),
                    reunion.getDureePrevue(),
                    reunion.getDureeReelle(),
                    reunion.getActionMiseEnPlace() != null ? reunion.getActionMiseEnPlace().toString() : "", // conversion Boolean → String
                    reunion.getDateAction() != null ? dateFormat.format(reunion.getDateAction()) : "",
                    reunion.getResponsableActionId() != null ? reunion.getResponsableActionId().toString() : "",
                    reunion.getClotureAction() != null ? reunion.getClotureAction().toString() : "", // conversion Boolean → String
                    reunion.getDateCloture() != null ? dateFormat.format(reunion.getDateCloture()) : ""
            });
        }


        csvWriter.close();

        // Réponse HTTP avec pièce jointe CSV
        byte[] csvBytes = stringWriter.toString().getBytes(StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDisposition(ContentDisposition.attachment().filename("reunions.csv").build());

        return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
    }



    @PutMapping("/{id}")
    public ResponseEntity<Void> updateReunion(
            @PathVariable Long id,
            @RequestBody @Valid ReunionRequest request
    ) {
        // Créer une nouvelle instance de ReunionRequest avec l'ID mis à jour
        ReunionRequest updatedRequest = new ReunionRequest(
                id,
                request.titre(),
                request.description(),
                request.date(),
                request.heure(),
                request.lieu(),
                request.pv(),
                request.documents(),
                request.userIds(),
                request.type(),
                request.clientId(),
                request.dateFin(),
                request.heureFin(),
                request.dureePrevue(),
                request.dureeReelle(),
                request.actionMiseEnPlace(),
                request.descriptionAction(),
                request.dateAction(),
                request.responsableActionId(),
                request.clotureAction(),
                request.dateCloture()
        );

        service.updateReunion(updatedRequest);
        return ResponseEntity.accepted().build();
    }

    @GetMapping
    public ResponseEntity<List<ReunionResponse>> findAll(){
        return ResponseEntity.ok(service.findAllReunions());
    }
    @GetMapping("/exist/{reunion-id}")
    public ResponseEntity<Boolean> existsById (
            @PathVariable("reunion-id") Long reunionId
    ){
        return ResponseEntity.ok(service.existisById(reunionId));
    }
    @GetMapping("/{reunion-id}")
    public ResponseEntity<ReunionResponse> findById(
            @PathVariable("reunion-id") Long reunionId
    ) {
        return ResponseEntity.ok(service.findById(reunionId));
    }

    @DeleteMapping ("/{reunion-id}")
    public ResponseEntity<Void> delete (
            @PathVariable("reunion-id") Long reunionId
    ){
        service.deleteReunion(reunionId);
        return ResponseEntity.accepted().build();
    }
    @GetMapping("/reunion/{reunionId}/participants")
    public ResponseEntity<List<UserResponse>> getParticipantsByReunionId(@PathVariable("reunionId") Long reunionId) {
        List<UserResponse> participants = userService.findByReunionId(reunionId.toString());
        return ResponseEntity.ok(participants);
    }
    @PostMapping("/{reunion-id}/documents")
    public ResponseEntity<Void> addDocumentToReunion(
            @PathVariable("reunion-id") Long reunionId,
            @RequestParam("file") MultipartFile file) throws IOException {

        service.addDocumentToReunion(reunionId, file);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{reunionId}/documents")
    public ResponseEntity<byte[]> getDocument(@PathVariable Long reunionId) {
        try {
            System.out.println("Fetching document for Reunion ID: " + reunionId);

            byte[] pdfContent = documentService.getDocumentPdfByReunionId(reunionId);

            if (pdfContent == null || pdfContent.length == 0) {
                System.out.println("Document not found for Reunion ID: " + reunionId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(("Document not found for reunion ID: " + reunionId).getBytes());
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=document.pdf");
            headers.add("Content-Type", "application/pdf");

            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
        } catch (IOException e) {
            System.out.println("Error retrieving document for Reunion ID: " + reunionId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error retrieving document: " + e.getMessage()).getBytes());
        }
    }
    @GetMapping("/{reunionId}/participants")
    public ResponseEntity<List<String>> getParticipantsNames(@PathVariable Long reunionId) {

            List<String> fullNames = service.getParticipantsFullNamesByReunionId(reunionId);
            return ResponseEntity.ok(fullNames);

    }
    @GetMapping("/generateReport/{id}")
    public ResponseEntity<byte[]> generateReport(@PathVariable Long id) {
        Optional<Reunion> reunionOptional = service.getReunionById(id);

        if (reunionOptional.isPresent()) {
            Reunion reunion = reunionOptional.get();

            // Récupération des utilisateurs associés
            List<UserResponse> users = userService.getUsersByIds(reunion.getUserIds());
            List<String> userNames = users.stream()
                    .map(user -> user.getNom() + " " + user.getPrenom())
                    .collect(Collectors.toList());
            String participantsFormatted = String.join(", ", userNames);

            // Date actuelle formatée
            String formattedDate = new SimpleDateFormat("dd MMM yyyy").format(new Date());

            // Préparation des paramètres
            String filePath = "C:\\Users\\fatou\\Desktop\\microservices\\services\\REUNION\\src\\main\\resources\\templates\\pv.jrxml";
            String logoPath = "C:\\Users\\fatou\\Desktop\\microservices\\services\\Interventions\\src\\main\\resources\\templates\\logo.png";

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("titre", reunion.getTitre());
            parameters.put("description", reunion.getDescription());
            parameters.put("date", formattedDate);
            parameters.put("heure", reunion.getHeure());
            parameters.put("lieu", reunion.getLieu());
            parameters.put("pv", reunion.getPv());
            parameters.put("documents", reunion.getDocuments());
            parameters.put("userIds", userNames);
            parameters.put("participants", participantsFormatted);
            parameters.put("type", reunion.getType() != null ? reunion.getType().name() : "");
            parameters.put("clientId", reunion.getClientId() != null ? reunion.getClientId().toString() : "");
            parameters.put("dateFin", reunion.getDateFin() != null ? new SimpleDateFormat("dd MMM yyyy").format(reunion.getDateFin()) : "");
            parameters.put("heureFin", reunion.getHeureFin());
            parameters.put("dureePrevue", reunion.getDureePrevue());
            parameters.put("dureeReelle", reunion.getDureeReelle());
            parameters.put("actionMiseEnPlace", reunion.getActionMiseEnPlace() != null ? (reunion.getActionMiseEnPlace() ? "Oui" : "Non") : "");
            parameters.put("descriptionAction", reunion.getDescriptionAction() != null ? reunion.getDescriptionAction() : ""); // ✅ Ajouté ici

            parameters.put("dateAction", reunion.getDateAction() != null ? new SimpleDateFormat("dd MMM yyyy").format(reunion.getDateAction()) : "");
            parameters.put("responsableActionId", reunion.getResponsableActionId() != null ? reunion.getResponsableActionId().toString() : "");
            parameters.put("clotureAction", reunion.getClotureAction() != null ? (reunion.getClotureAction() ? "Oui" : "Non") : "");
            parameters.put("dateCloture", reunion.getDateCloture() != null ? new SimpleDateFormat("dd MMM yyyy").format(reunion.getDateCloture()) : "");
            parameters.put("LOGO_PATH", logoPath);

            try {
                JasperReport report = JasperCompileManager.compileReport(filePath);
                JRDataSource dataSource = new JREmptyDataSource();
                JasperPrint print = JasperFillManager.fillReport(report, parameters, dataSource);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                JasperExportManager.exportReportToPdfStream(print, byteArrayOutputStream);

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=pv" + id + ".pdf")
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(byteArrayOutputStream.toByteArray());

            } catch (JRException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
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
