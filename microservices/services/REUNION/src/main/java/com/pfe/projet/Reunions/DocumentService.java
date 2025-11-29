package com.pfe.projet.Reunions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

@Service
public class DocumentService {
    private final ReunionRepository reunionRepository;

    @Autowired
    public DocumentService(ReunionRepository reunionRepository) {
        this.reunionRepository = reunionRepository;
    }

    public byte[] getDocumentPdfByReunionId(Long reunionId) throws IOException {
        Reunion reunion = reunionRepository.findById(reunionId).orElse(null);

        if (reunion == null || reunion.getDocuments() == null || reunion.getDocuments().isEmpty()) {
            System.out.println("No document found for reunion ID: " + reunionId);
            return null;
        }

        String documents = reunion.getDocuments();
        System.out.println("Documents field for Reunion ID " + reunionId + ": " + documents);
        if (isBase64(documents)) {
            try {
                return Base64.getDecoder().decode(documents);
            } catch (IllegalArgumentException e) {
                System.out.println("Error decoding Base64 document for Reunion ID " + reunionId);
                throw new IOException("Error decoding the document", e);
            }
        } else {
            File file = new File(documents);
            if (!file.exists()) {
                System.out.println("File not found for Reunion ID: " + reunionId);
                return null;
            }
            System.out.println("Reading file for Reunion ID: " + reunionId);
            return Files.readAllBytes(file.toPath());
        }
    }

    private boolean isBase64(String value) {
        try {
            Base64.getDecoder().decode(value);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid Base64 document format.");
            return false;
        }
    }

}