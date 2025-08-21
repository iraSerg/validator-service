package com.example.validatorservice.persistence.repository;


import com.example.validatorservice.persistence.model.ProcessedEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface ProcessedEventRepository extends MongoRepository<ProcessedEvent, UUID> {
    boolean existsByMessageId(String id);
}
