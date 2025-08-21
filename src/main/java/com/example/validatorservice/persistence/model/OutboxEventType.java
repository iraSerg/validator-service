package com.example.validatorservice.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OutboxEventType {
    RECEIPT_VALIDATED_EVENT("receipt_validated_event");
    private final String value;

}
