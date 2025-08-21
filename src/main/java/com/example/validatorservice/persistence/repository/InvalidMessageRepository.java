package com.example.validatorservice.persistence.repository;

import com.example.validatorservice.persistence.model.InvalidMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InvalidMessageRepository extends MongoRepository<InvalidMessage, String> {
}
