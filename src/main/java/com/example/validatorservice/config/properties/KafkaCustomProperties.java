package com.example.validatorservice.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@ConfigurationProperties(prefix = "custom.kafka")
@Data
@Component
public class KafkaCustomProperties {

    private Map<String, TopicConfig> topics;
    private ErrorHandlerProperties errorHandler;

    @Data
    public static class TopicConfig {
        private int partitions;
        private int replicas;
        private int minInsyncReplicas;
    }

    @Data
    public static class ErrorHandlerProperties {
        private long backoffInterval;
        private long maxAttempts;
    }

    public TopicConfig getTopicConfig(String topicName) {
        TopicConfig config = topics.get(topicName);
        if (config == null) {
            throw new IllegalArgumentException("No topic config found for topic: " + topicName);
        }
        return config;
    }
}