package com.pfe.projet.Reunions;

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
    private ReunionService reunionService;

    @Autowired
    private ReunionMapper reunionMapper;
    @Autowired
    private ReunionRepository reunionRepository;
    public byte[] exportReunionsToCsv() throws IOException {
        List<ReunionResponse> reunionResponses = reunionService.findAllReunions();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(byteArrayOutputStream))) {
            writer.writeNext(new String[]{"ID", "Titre", "Description", "Date", "Heure", "Lieu", "PV"});
            for (ReunionResponse reunionResponse : reunionResponses) {
                String[] row = {
                        reunionResponse.id().toString(),
                        reunionResponse.titre(),
                        reunionResponse.description(),
                        reunionResponse.date().toString(),
                        reunionResponse.heure(),
                        reunionResponse.lieu(),
                        reunionResponse.pv()
                };
                writer.writeNext(row);
            }
        }

        return byteArrayOutputStream.toByteArray();
    }
    public List<Reunion> getReunionsList() {
        // Retrieve all the reunion objects from the database
        return reunionRepository.findAll();
    }
}
