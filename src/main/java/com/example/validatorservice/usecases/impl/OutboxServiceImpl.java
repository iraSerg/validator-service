package com.example.validatorservice.usecases.impl;


import com.example.validatorservice.outbox.OutboxProcessor;
import com.example.validatorservice.persistence.model.OutboxEvent;
import com.example.validatorservice.persistence.model.OutboxEventStatus;
import com.example.validatorservice.persistence.model.OutboxEventType;
import com.example.validatorservice.persistence.repository.OutboxRecordRepositoryCustom;
import com.example.validatorservice.persistence.repository.OutboxRepository;
import com.example.validatorservice.producer.RetryableExceptionStrategyResolver;
import com.example.validatorservice.usecases.OutboxErrorEventService;
import com.example.validatorservice.usecases.OutboxRecordStatusService;
import com.example.validatorservice.usecases.OutboxService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class OutboxServiceImpl implements OutboxService {
    private final OutboxRepository outboxRepository;
    private final OutboxRecordRepositoryCustom customOutboxRecordRepository;
    private Map<OutboxEventType, OutboxProcessor> outboxProcessorMap;
    private final List<OutboxProcessor> outboxProcessorList;
    private final RetryableExceptionStrategyResolver resolver;
    private final OutboxErrorEventService errorRecordService;
    private final OutboxRecordStatusService statusService;

    @PostConstruct
    public void init() {
        outboxProcessorMap = outboxProcessorList.stream()
                .collect(Collectors.toMap(OutboxProcessor::getRecordType, Function.identity()));
    }

    @Value("${outbox.batch-size}")
    private Integer batchSize;

    private static final Set<String> pendingStatuses = Set.of(
            OutboxEventStatus.NEW.getStatus(),
            OutboxEventStatus.FAILED.getStatus()
    );

    @Override

    public void create(String payload, OutboxEventType type, Long messageKey, UUID messageId) {
        OutboxEvent record = new OutboxEvent();
        record.setPayload(payload);
        record.setType(type);
        record.setMessageKey(messageKey);
        record.setMessageId(messageId);
        record.setStatus(OutboxEventStatus.NEW.getStatus());
        record.setCreatedAt(LocalDateTime.now());
        outboxRepository.save(record);
    }

    @Override
    @Transactional(transactionManager = "transactionManager")
    public void processAll() {
        customOutboxRecordRepository.selectForProcessing(pendingStatuses, batchSize)
                .forEach(this::process);
    }

    private void process(OutboxEvent record) {
        OutboxProcessor processor = outboxProcessorMap.get(record.getType());
        if (processor == null) {
            throw new RuntimeException("No OutboxProcessor found for type " + record.getType());
        }
        processor.process(record).whenComplete((result, ex) -> {
            if (ex != null) {
                if (isRecoverable(ex)) {
                    statusService.updateStatus(record.getId(), OutboxEventStatus.FAILED);
                } else {
                    errorRecordService.sendErrorMessage(record, ex);
                    statusService.updateStatus(record.getId(), OutboxEventStatus.ERROR);
                }
            } else {
                statusService.updateStatus(record.getId(), OutboxEventStatus.SENT);
            }
        });

    }

    private boolean isRecoverable(Throwable e) {
        Throwable cause = e.getCause() != null ? e.getCause() : e;
        return resolver.resolve(cause).isPresent();
    }
}
