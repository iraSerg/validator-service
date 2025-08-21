package com.example.validatorservice.outbox.scheduler;


import com.example.validatorservice.usecases.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxScheduler {
    private final OutboxService outboxService;

    @Scheduled(fixedDelay = 10000)
    public void runProcessing() {
        log.info("Starting outbox processing cycle");
        outboxService.processAll();
        log.info("Finished outbox processing cycle");
    }
}
