package com.example.validatorservice.controller;


import com.example.validatorservice.event.ItemDto;
import com.example.validatorservice.event.ReceiptValidatedEvent;
import com.example.validatorservice.util.Topic;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TestController {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @GetMapping
    @Transactional(transactionManager = "transactionManager")
    public String test() {
        ItemDto item1 = new ItemDto(
                "Товар 1",
                new BigDecimal("199.99"),
                new BigDecimal("2"),
                new BigDecimal("399.98"),
                1,
                new BigDecimal("399.98"),
                0,
                "CASH"
        );

        ItemDto item2 = new ItemDto(
                "Товар 2",
                new BigDecimal("120.50"),
                new BigDecimal("1"),
                new BigDecimal("120.50"),
                2,
                new BigDecimal("120.50"),
                1,
                "CARD"
        );


        ReceiptValidatedEvent event = new ReceiptValidatedEvent(
                1001L,
                5002L,
                LocalDateTime.now(),
                List.of(item1, item2)
        );

        ProducerRecord<String, Object> record = new ProducerRecord<>(Topic.RECEIPT_VALIDATE_INPUT_TOPIC_EVENTS, String.valueOf(1L), event);
        record.headers().add("messageId", UUID.randomUUID().toString().getBytes());
        kafkaTemplate.send(record);
        return "отправлено";
    }
}
