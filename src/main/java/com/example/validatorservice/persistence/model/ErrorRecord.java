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
@Document(collection = "error_records")
@EqualsAndHashCode
@ToString
public class ErrorRecord {

    @Id
    private String id;
    private String record;
    private String error;
    private LocalDateTime createdAt;
}