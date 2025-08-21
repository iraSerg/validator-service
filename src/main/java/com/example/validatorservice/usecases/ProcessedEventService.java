package com.example.validatorservice.usecases;

public interface ProcessedEventService {
    boolean isDuplicate(String messageId);

    void save(String messageId);
}
