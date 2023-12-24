package com.example.ai_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;
import java.util.UUID;


@Component
public class KafkaConsumerComponent {

    @Autowired
    private TextToImageService textToImageService;

    @Autowired
    private DataTransferMapper dataTransferMapper;

    @Autowired
    private KafkaProducerComponent kafkaProducerComponent;

    @Autowired
    private S3Client s3Client;

    private final String bucketName = "tsai-images";

    @KafkaListener(topics = "input", groupId = "ai_service")
    public void listen(String message) {
        String userId = dataTransferMapper.getStringFromJsonLikeString(message, "userId");
        String text = dataTransferMapper.getStringFromJsonLikeString(message, "message");
        System.out.println("Received message in group - input: " + userId + " " + text);

        textToImageService
            .callTextToImage(text)
            .thenAccept(imageBytes -> {
                if (imageBytes != null) {
                    System.out.println("Image created");
                    String key = UUID.randomUUID().toString().replace("-", "");
                    PutObjectRequest putObjectRequest =
                        PutObjectRequest.builder().bucket(bucketName).key(key).contentType("image/png").build();
                    s3Client.putObject(putObjectRequest, RequestBody.fromBytes(imageBytes));

                    S3Presigner presigner = S3Presigner.builder().build();

                    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build();

                    GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(10))
                        .getObjectRequest(getObjectRequest)
                        .build();

                    PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(getObjectPresignRequest);
                    String url = presignedGetObjectRequest.url().toString();

                    presigner.close();

                    String serializedString = dataTransferMapper.getJsonLikeString(userId, url);
                    kafkaProducerComponent.sendMessage(serializedString);
                } else {
                    System.out.println("Image is null");
                }
            });
    }
}

