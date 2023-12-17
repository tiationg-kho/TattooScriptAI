package com.example.ai_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


@Component
public class KafkaConsumerComponent {

    @Autowired
    private TextToImageService textToImageService;

    @Autowired
    private DataTransferMapper dataTransferMapper;

    @Autowired
    private KafkaProducerComponent kafkaProducerComponent;

    @KafkaListener(topics = "input", groupId = "ai_service")
    public void listen(String message) {
        String userId = dataTransferMapper.getStringFromJsonLikeString(message, "userId");
        String text = dataTransferMapper.getStringFromJsonLikeString(message, "message");
        System.out.println("Received message in group - input: " + userId + " " + text);

        textToImageService
            .callTextToImage(text)
            .thenAccept(imageBytes -> {
                if (imageBytes != null) {
                    File file = new File("tattoo.png");
                    try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                        fileOutputStream.write(imageBytes);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Image created");
                    String serializedString = dataTransferMapper.getJsonLikeString(userId, text);
                    kafkaProducerComponent.sendMessage(serializedString);
                } else {
                    System.out.println("Image is null");
                }
            });
    }
}

