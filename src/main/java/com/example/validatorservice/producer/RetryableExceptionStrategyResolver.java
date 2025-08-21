package com.example.validatorservice.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RetryableExceptionStrategyResolver {
    private final List<KafkaExceptionHandler> handlers;

    public Optional<KafkaExceptionHandler> resolve(Throwable exception) {
        return handlers.stream()
                .filter(handler -> handler.supports(exception))
                .findFirst();
    }
}
