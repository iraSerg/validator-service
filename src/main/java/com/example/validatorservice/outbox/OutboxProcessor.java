package com.example.validatorservice.outbox;

import com.example.validatorservice.persistence.model.OutboxEvent;
import com.example.validatorservice.persistence.model.OutboxEventType;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

public interface OutboxProcessor {
    CompletableFuture<SendResult<String, Object>> process(OutboxEvent eventType);

    OutboxEventType getRecordType();
}
