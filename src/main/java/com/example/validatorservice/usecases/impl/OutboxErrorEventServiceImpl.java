package com.example.validatorservice.usecases.impl;

import com.example.validatorservice.mapper.EventMapper;
import com.example.validatorservice.persistence.model.ErrorRecord;
import com.example.validatorservice.persistence.model.OutboxEvent;
import com.example.validatorservice.producer.MessageProducer;
import com.example.validatorservice.usecases.OutboxErrorEventService;
import com.example.validatorservice.util.Topic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OutboxErrorEventServiceImpl implements OutboxErrorEventService {
    private final MessageProducer messageProducer;
    private final EventMapper eventMapper;

    @Override
    public void sendErrorMessage(OutboxEvent outboxRecord, Throwable throwable) {
        ErrorRecord errorRecord = new ErrorRecord();
        String outboxRecordStr = eventMapper.convertToString(outboxRecord);
        errorRecord.setRecord(outboxRecordStr);
        errorRecord.setError(throwable.getMessage());
        errorRecord.setCreatedAt(LocalDateTime.now());
        messageProducer.sendMessage(Topic.RECEIPT_VALIDATE_ERROR_RECORDS, errorRecord);
    }
}
