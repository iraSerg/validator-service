package com.example.validatorservice.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OutboxEventStatus {
    NEW("new"),
    SENT("sent"),
    PROCESSING("processing"),
    FAILED("failed"),
    ERROR("error");

    private final String status;
}
