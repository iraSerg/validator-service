package com.example.validatorservice.persistence.repository;

import com.example.validatorservice.persistence.model.OutboxEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface OutboxRepository extends MongoRepository<OutboxEvent, UUID> {
}
