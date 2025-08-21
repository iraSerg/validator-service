package com.example.validatorservice.persistence.repository;

import com.example.validatorservice.persistence.model.OutboxEvent;

import java.util.List;
import java.util.Set;

public interface OutboxRecordRepositoryCustom {
    List<OutboxEvent> selectForProcessing(Set<String> statuses, int batchSize);

    void updateStatus(String id, String status);
}