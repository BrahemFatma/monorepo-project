package com.pfe.projet.Reunions;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KafkaProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(ReunionConfirmation reunionConfirmation) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(reunionConfirmation);
            System.out.println("Envoi de message Kafka : " + json);

            Message<String> message = MessageBuilder
                    .withPayload(json)
                    .setHeader(KafkaHeaders.TOPIC, "reunion-topic")
                    .build();

            kafkaTemplate.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
