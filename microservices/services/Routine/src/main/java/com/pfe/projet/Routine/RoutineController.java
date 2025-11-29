package com.pfe.projet.Routine;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import com.pfe.projet.user.UserResponse;
@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("api/v1/Routines")
public class RoutineController {
    @Autowired
    private CsvExportService csvExportService;
    private final RoutineService service;

    @Autowired
    public RoutineController(RoutineService service) {
        this.service = service;
        this.csvExportService = csvExportService;
    }
    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportRoutinesToCsv() throws IOException {
        byte[] csvContent = csvExportService.exportRoutinesToCsv();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=routines.csv")
                .header(HttpHeaders.CONTENT_TYPE, "text/csv")
                .body(csvContent);
    }

    @PostMapping
    public ResponseEntity<Long> createRoutine(
            @RequestBody @Valid RoutineRequest request
    ) {
        return ResponseEntity.ok(service.createRoutine(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateRoutine(
            @PathVariable Long id,
            @RequestBody @Valid RoutineRequest request
    ){
        service.updateRoutine(id, request);
        return ResponseEntity.accepted().build();
    }


    @GetMapping
    public ResponseEntity<List<RoutineResponse>> findAll() {
        return ResponseEntity.ok(service.findAllRoutines());
    }

    @GetMapping("/exist/{routine-id}")
    public ResponseEntity<Boolean> existsById(
            @PathVariable("routine-id") Long routineId
    ) {
        return ResponseEntity.ok(service.existsById(routineId));
    }

    @GetMapping("/{routine-id}")
    public ResponseEntity<RoutineResponse> findById(
            @PathVariable("routine-id") Long routineId
    ) {
        return ResponseEntity.ok(service.findById(routineId));
    }

    @DeleteMapping("/{routine-id}")
    public ResponseEntity<Void> delete(
            @PathVariable("routine-id") Long routineId
    ) {
        service.deleteRoutine(routineId);
        return ResponseEntity.accepted().build();
    }
    @GetMapping("/loadResponsable/{routineIds}")
    public ResponseEntity<List<UserResponse>> loadResponsablesForRoutines(
            @PathVariable List<Long> routineIds) {
        // Récupérer les responsables pour les IDs de routines
        List<UserResponse> responsables = service.loadResponsableForRoutine(routineIds);

        // Retourner les responsables récupérés
        return ResponseEntity.ok(responsables);
    }






}