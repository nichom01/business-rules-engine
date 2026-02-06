package com.businessrules.listener;

import com.businessrules.model.ProcessingRequest;
import com.businessrules.model.ProcessingResult;
import com.businessrules.service.RulesProcessingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class SqsMessageListener {

    private static final Logger log = LoggerFactory.getLogger(SqsMessageListener.class);
    private final RulesProcessingService rulesProcessingService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SqsMessageListener(RulesProcessingService rulesProcessingService) {
        this.rulesProcessingService = rulesProcessingService;
    }

    @SqsListener(value = "${sqs.queue-name:business-rules-queue}")
    public void processMessage(@Payload String message) {
        try {
            log.info("Received message from SQS: {}", message);
            
            ProcessingRequest request = objectMapper.readValue(message, ProcessingRequest.class);
            
            ProcessingResult result = rulesProcessingService.processObjects(request.getObjects());
            
            log.info("Processing completed. Success: {}, Errors: {}", 
                    result.isSuccess(), result.getErrors().size());
            
            if (!result.isSuccess()) {
                log.warn("Processing failed with errors: {}", result.getErrors());
            }
            
        } catch (Exception e) {
            log.error("Error processing SQS message: {}", message, e);
            throw new RuntimeException("Failed to process message", e);
        }
    }
}
