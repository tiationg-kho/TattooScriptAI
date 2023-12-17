package com.example.ai_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
public class DataTransferMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public String getStringFromJsonLikeString(String serializedString, String key) {
        JsonNode jsonNode = objectMapper.readTree(serializedString);
        return jsonNode.get(key).asText();
    }

    public String getJsonLikeString(String userId, String message) {
        ObjectNode json = objectMapper.createObjectNode();
        json.put("userId", userId);
        json.put("message", message);
        try {
            return objectMapper.writeValueAsString(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

}
