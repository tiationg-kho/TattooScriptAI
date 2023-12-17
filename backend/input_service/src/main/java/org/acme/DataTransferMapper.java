package org.acme;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DataTransferMapper {
    private ObjectMapper objectMapper = new ObjectMapper();

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

    public String getStringFromJsonLikeString(String serializedString, String key) throws JsonMappingException, JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(serializedString);
        return jsonNode.get(key).asText();
    }
  
}
