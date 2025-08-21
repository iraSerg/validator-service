package com.example.validatorservice.mapper;


import com.example.validatorservice.exception.JsonConversionException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventMapper {
    private final ObjectMapper objectMapper;

    public <T> T convertFromString(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize json: {}", json, e);
            throw new JsonConversionException("Failed to deserialize json", e);
        }
    }

    public <T> String convertToString(T record) {
        try {
            return objectMapper.writeValueAsString(record);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize record: {}", record, e);
            throw new JsonConversionException("Failed to serialize record", e);
        }
    }
}
