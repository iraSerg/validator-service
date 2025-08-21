package com.example.validatorservice.usecases.impl;


import com.example.validatorservice.config.properties.PathProperties;
import com.example.validatorservice.exception.SchemaValidationException;
import com.example.validatorservice.usecases.JsonValidatorService;
import lombok.extern.slf4j.Slf4j;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
public class JsonSchemaValidatorServiceImpl implements JsonValidatorService {

    private Schema schema;

    public JsonSchemaValidatorServiceImpl(PathProperties pathProperties,
                                          @Qualifier("webApplicationContext") ResourceLoader resourceLoader) {
        String schemaPath = pathProperties.getJsonSchema();
        try (InputStream inputStream = resourceLoader.getResource(schemaPath).getInputStream()) {
            JSONObject jsonSchema = new JSONObject(new JSONTokener(inputStream));
            this.schema = SchemaLoader.load(jsonSchema);

        } catch (IOException e) {
            log.error("Failed to load schema file: {}", schemaPath, e);
            throw new SchemaValidationException("Failed to load schema file: " + schemaPath, e);

        } catch (Exception e) {
            log.error("Unexpected error loading JSON schema: {}", schemaPath, e);
            throw new SchemaValidationException("Failed to load JSON schema", e);
        }
    }

    public void validate(String rawJson) {
        try {
            JSONObject json = new JSONObject(rawJson);
            schema.validate(json);
        } catch (ValidationException e) {
            throw new SchemaValidationException("JSON validation error: " + e.getMessage());
        }
    }
}