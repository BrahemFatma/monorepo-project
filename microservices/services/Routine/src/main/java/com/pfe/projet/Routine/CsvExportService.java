package com.pfe.projet.Routine;

import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

@Service
public class CsvExportService {

    @Autowired
    private RoutineService routineService;

    public byte[] exportRoutinesToCsv() throws IOException {
        List<RoutineResponse> routineResponses = routineService.findAllRoutines();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (CSVWriter writer = new CSVWriter(
                new OutputStreamWriter(byteArrayOutputStream))){

            writer.writeNext(new String[]{"ID", "Titre", "Description", "Date Début", "Date Fin", "ResponsableID", "Statut"});

            for (RoutineResponse routineResponse : routineResponses) {
                String[] row = {
                        routineResponse.id().toString(),
                        routineResponse.nom(),
                        routineResponse.description(),
                        routineResponse.dateDebut().toString(),
                        routineResponse.dateFin().toString(),
                        routineResponse.userId().toString(),
                        routineResponse.statut().name(),

                };
                writer.writeNext(row);
            }
        }

        return byteArrayOutputStream.toByteArray();
    }
}
