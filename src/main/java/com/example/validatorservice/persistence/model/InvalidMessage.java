package com.example.validatorservice.persistence.model;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "invalid_messages")
@EqualsAndHashCode
@ToString
public class InvalidMessage {
    @Id
    private String id;
    private String rawMessage;
    private String error;
}
