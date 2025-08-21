package com.example.validatorservice.producer;

public interface KafkaExceptionHandler {
    boolean supports(Throwable exception);

}
