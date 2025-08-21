package com.example.validatorservice.usecases;

import com.example.validatorservice.persistence.model.OutboxEvent;

public interface OutboxErrorEventService {
    void sendErrorMessage(OutboxEvent record, Throwable throwable);
}
