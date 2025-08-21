package com.example.validatorservice.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;


    public CompletableFuture<SendResult<String, Object>> sendMessage(String topic,
                                                                     Object message,
                                                                     String messageId,
                                                                     String messageKey

    ) {
        ProducerRecord<String, Object> record = new ProducerRecord<>(topic, messageKey, message);
        record.headers().add("messageId", messageId.getBytes(StandardCharsets.UTF_8));
        return kafkaTemplate.send(record);
    }

    public CompletableFuture<SendResult<String, Object>> sendMessage(String topic, Object message) {
        return kafkaTemplate.send(topic, message);
    }
}
