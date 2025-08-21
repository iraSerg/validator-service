package com.example.validatorservice.usecases;

import com.example.validatorservice.persistence.model.OutboxEventStatus;

public interface OutboxRecordStatusService {
    void updateStatus(String id, OutboxEventStatus status);
}
