package com.example.validatorservice.persistence.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "outbox")
@ToString
@EqualsAndHashCode
public class OutboxEvent {
    @Id
    private String id;
    private OutboxEventType type;
    private UUID messageId;
    private Long messageKey;
    private String payload;
    private String status;
    private LocalDateTime createdAt;
}
