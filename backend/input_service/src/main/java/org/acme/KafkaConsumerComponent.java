package org.acme;

import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.smallrye.reactive.messaging.annotations.Blocking;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class KafkaConsumerComponent {

    @Inject
    private SocketMessagingService socketMessagingService;

    @Inject
    private DataTransferMapper dataTransferMapper;

    @Incoming("output")
    @Blocking
    public CompletionStage<Void> consume(KafkaRecord<String, String> record) throws JsonMappingException, JsonProcessingException {
        String serializedString = record.getPayload();
        String userId = dataTransferMapper.getStringFromJsonLikeString(serializedString, "userId");
        String message = dataTransferMapper.getStringFromJsonLikeString(serializedString, "message");
        System.out.println("Received message in input_service: " + message);
        socketMessagingService.sendMessageToUser(userId, message);
        return record.ack();
    }


  
}
