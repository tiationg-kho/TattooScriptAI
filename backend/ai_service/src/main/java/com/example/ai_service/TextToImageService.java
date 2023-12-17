package com.example.ai_service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;


@Service
public class TextToImageService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${STABILITY_API_KEY}")
    private String STABILITY_API_KEY;

    @Value("${STABILITY_API_URL}")
    private String STABILITY_API_URL;

    @Value("${STABILITY_API_PREPROMPT}")
    private String STABILITY_API_PREPROMPT;

    @Async("taskExecutor")
    public CompletableFuture<byte[]> callTextToImage(String text) {
        try {
            System.out.println("Executing callTextToImageAsync in thread: " + Thread.currentThread().getName());
            System.out.println("Start calling API " + LocalDateTime.now());

            final String url = STABILITY_API_URL + "/v1/generation/stable-diffusion-xl-1024-v1-0/text-to-image";
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(STABILITY_API_KEY);
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.IMAGE_PNG));
            JSONObject requestBody = new JSONObject();
            JSONObject promptObject = new JSONObject();
            String prompt = STABILITY_API_PREPROMPT + text;
            System.out.println(prompt);
            promptObject.put("text", prompt);
            JSONArray promptObjects = new JSONArray();
            promptObjects.put(promptObject);
            requestBody.put("text_prompts", promptObjects);

            ResponseEntity<byte[]> response =
                restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(requestBody.toMap(), headers), byte[].class);

            if (response.getStatusCode() != HttpStatus.OK || !response.hasBody()) {
                throw new RuntimeException(response.toString());
            }

            System.out.println("End calling API " + LocalDateTime.now());

            return CompletableFuture.completedFuture(response.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }
    }
}

