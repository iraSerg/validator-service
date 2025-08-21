package com.example.validatorservice.producer;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeoutException;

@Component
@Slf4j
public class TimeOutExceptionHandler implements KafkaExceptionHandler {
    @Override
    public boolean supports(Throwable exception) {
        return exception instanceof TimeoutException;
    }
}
