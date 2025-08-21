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

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "processed_events")
@ToString
@EqualsAndHashCode
public class ProcessedEvent {
    @Id
    private String id;
    private String messageId;
    private LocalDateTime createdAt;

}
