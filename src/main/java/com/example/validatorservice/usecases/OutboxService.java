package com.example.validatorservice.usecases;


import com.example.validatorservice.persistence.model.OutboxEventType;

import java.util.UUID;

public interface OutboxService {
    void create(String payload, OutboxEventType type,
                Long messageKey,
                UUID messageId);

    void processAll();
}
