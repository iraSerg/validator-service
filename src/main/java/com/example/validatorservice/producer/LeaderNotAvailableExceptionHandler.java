package com.example.validatorservice.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.LeaderNotAvailableException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LeaderNotAvailableExceptionHandler implements KafkaExceptionHandler {
    @Override
    public boolean supports(Throwable exception) {
        return exception instanceof LeaderNotAvailableException;
    }
}
