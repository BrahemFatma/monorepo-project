package com.pfe.projet.User;

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
    private UserService userService;

    public byte[] exportUsersToCsv() throws IOException {
        List<UserResponse> userResponses = userService.findAllUsers();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (CSVWriter writer = new CSVWriter(
                new OutputStreamWriter(byteArrayOutputStream))) {
            writer.writeNext(new String[]{"ID", "Nom", "Prénom", "Email", "Mot de Passe", "Role"});
            for (UserResponse userResponse : userResponses) {
                String[] row = {
                        userResponse.getId().toString(),
                        userResponse.getNom(),
                        userResponse.getPrenom(),
                        userResponse.getEmail(),
                        userResponse.getMotDePasse(),
                        userResponse.getRole(),

                };
                writer.writeNext(row);
            }
        }

        return byteArrayOutputStream.toByteArray();
    }
}
