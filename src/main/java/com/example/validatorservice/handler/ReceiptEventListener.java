package com.example.validatorservice.handler;

import com.example.validatorservice.event.ReceiptValidatedEvent;
import com.example.validatorservice.exception.NonRetryableException;
import com.example.validatorservice.exception.SchemaValidationException;
import com.example.validatorservice.mapper.EventMapper;
import com.example.validatorservice.persistence.model.InvalidMessage;
import com.example.validatorservice.persistence.model.OutboxEventType;
import com.example.validatorservice.persistence.repository.InvalidMessageRepository;
import com.example.validatorservice.usecases.JsonValidatorService;
import com.example.validatorservice.usecases.OutboxService;
import com.example.validatorservice.usecases.ProcessedEventService;
import com.example.validatorservice.util.Topic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@KafkaListener(topics = Topic.RECEIPT_VALIDATE_INPUT_TOPIC_EVENTS)
@RequiredArgsConstructor
@Slf4j
public class ReceiptEventListener {
    private final ProcessedEventService processedEventService;
    private final JsonValidatorService validatorService;
    private final InvalidMessageRepository invalidMessageRepository;
    private final OutboxService outboxService;
    private final EventMapper eventMapper;

    @KafkaHandler
    @Transactional(transactionManager = "transactionManager")
    public void handle(@Payload String message, @Header("messageId") String messageId) {
        if (messageId == null || messageId.isEmpty()) {
            log.error("MessageId is null or empty");
            throw new NonRetryableException("Message id is null");
        }
        if (processedEventService.isDuplicate(messageId)) {
            log.error("Duplicate receipt event received");
            return;
        }
        ReceiptValidatedEvent event = null;
        try {
            validatorService.validate(message);
            log.info("Successfully validate message with id={}", messageId);
            event = eventMapper.convertFromString(message, ReceiptValidatedEvent.class);
            outboxService.create(
                    message,
                    OutboxEventType.RECEIPT_VALIDATED_EVENT,
                    event.userId(), UUID.randomUUID());
        } catch (SchemaValidationException e) {
            log.error("Schema validation error");
            InvalidMessage invalidMessage = new InvalidMessage();
            invalidMessage.setRawMessage(message);
            invalidMessage.setError(e.getMessage());
            invalidMessageRepository.save(invalidMessage);
        }

        processedEventService.save(messageId);
        log.info("Successfully saved messageId={}", messageId);

    }
}
