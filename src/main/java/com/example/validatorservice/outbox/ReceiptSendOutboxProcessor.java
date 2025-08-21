package com.example.validatorservice.outbox;


import com.example.validatorservice.event.ReceiptValidatedEvent;
import com.example.validatorservice.mapper.EventMapper;
import com.example.validatorservice.persistence.model.OutboxEvent;
import com.example.validatorservice.persistence.model.OutboxEventType;
import com.example.validatorservice.producer.MessageProducer;
import com.example.validatorservice.util.Topic;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class ReceiptSendOutboxProcessor implements OutboxProcessor {
    private final EventMapper recordMapper;
    private final MessageProducer messageProducer;

    @Override
    public CompletableFuture<SendResult<String, Object>> process(OutboxEvent event) {
        var receiptValidatedEvent = recordMapper.convertFromString(event.getPayload(), ReceiptValidatedEvent.class);

        return messageProducer.sendMessage(
                Topic.RECEIPT_VALIDATE_OUTPUT_TOPIC_EVENTS,
                receiptValidatedEvent,
                event.getMessageId().toString(),
                event.getMessageKey().toString());
    }


    @Override
    public OutboxEventType getRecordType() {
        return OutboxEventType.RECEIPT_VALIDATED_EVENT;
    }
}
