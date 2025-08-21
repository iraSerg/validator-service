package com.example.validatorservice.usecases.impl;


import com.example.validatorservice.persistence.model.OutboxEventStatus;
import com.example.validatorservice.persistence.repository.OutboxRecordRepositoryCustom;
import com.example.validatorservice.usecases.OutboxRecordStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OutboxRecordStatusServiceImpl implements OutboxRecordStatusService {
    private final OutboxRecordRepositoryCustom outboxRepository;

    @Transactional(transactionManager = "transactionManager")
    public void updateStatus(String id, OutboxEventStatus status) {
        outboxRepository.updateStatus(id, status.getStatus());
    }
}
