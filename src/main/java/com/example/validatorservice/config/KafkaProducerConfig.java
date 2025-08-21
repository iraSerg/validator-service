package com.example.validatorservice.config;


import com.example.validatorservice.config.properties.KafkaCustomProperties;
import com.example.validatorservice.util.Topic;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.transaction.ChainedKafkaTransactionManager;
import org.springframework.kafka.transaction.KafkaTransactionManager;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {
    private final KafkaProperties kafkaProperties;
    private final KafkaCustomProperties kafkaCustomProperties;

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProperties.getProducer().getKeySerializer());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProperties.getProducer().getValueSerializer());
        props.put(ProducerConfig.ACKS_CONFIG, kafkaProperties.getProducer().getAcks());

        Map<String, String> additionalProperties = kafkaProperties.getProducer().getProperties();
        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, additionalProperties.get("delivery.timeout.ms"));
        props.put(ProducerConfig.LINGER_MS_CONFIG, additionalProperties.get("linger.ms"));
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, additionalProperties.get("request.timeout.ms"));
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, additionalProperties.get("enable.idempotence"));
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,
                additionalProperties.get("max.in.flight.requests.per.connection"));
        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, kafkaProperties.getProducer().getTransactionIdPrefix());
        return props;
    }

    @Bean
    ProducerFactory<String, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public NewTopic topicInput() {
        var topicProperties = kafkaCustomProperties.getTopicConfig(Topic.RECEIPT_VALIDATE_INPUT_TOPIC_EVENTS);
        return TopicBuilder
                .name(Topic.RECEIPT_VALIDATE_INPUT_TOPIC_EVENTS)
                .partitions(topicProperties.getPartitions())
                .replicas(topicProperties.getReplicas())
                .configs(Map.of("min.insync.replicas", String.valueOf(topicProperties.getMinInsyncReplicas())))
                .build();
    }

    @Bean
    public NewTopic topicOutput() {
        var topicProperties = kafkaCustomProperties.getTopicConfig(Topic.RECEIPT_VALIDATE_OUTPUT_TOPIC_EVENTS);
        return TopicBuilder
                .name(Topic.RECEIPT_VALIDATE_OUTPUT_TOPIC_EVENTS)
                .partitions(topicProperties.getPartitions())
                .replicas(topicProperties.getReplicas())
                .configs(Map.of("min.insync.replicas", String.valueOf(topicProperties.getMinInsyncReplicas())))
                .build();
    }

    @Bean
    public NewTopic errorTopic() {
        var topicProperties = kafkaCustomProperties.getTopicConfig(Topic.RECEIPT_VALIDATE_ERROR_RECORDS);
        return TopicBuilder
                .name(Topic.RECEIPT_VALIDATE_ERROR_RECORDS)
                .partitions(topicProperties.getPartitions())
                .replicas(topicProperties.getReplicas())
                .configs(Map.of("min.insync.replicas", String.valueOf(topicProperties.getMinInsyncReplicas())))
                .build();
    }

    @Bean
    public MongoTransactionManager mongoTransactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

    @Bean
    public KafkaTransactionManager<String, Object> kafkaTransactionManager(ProducerFactory<String, Object> producerFactory) {
        return new KafkaTransactionManager<>(producerFactory);
    }

    @Bean("transactionManager")
    public ChainedKafkaTransactionManager<String, Object> chainedKafkaTransactionManager(
            MongoTransactionManager mongoTransactionManager,
            KafkaTransactionManager<String, Object> kafkaTransactionManager) {

        return new ChainedKafkaTransactionManager<>(mongoTransactionManager, kafkaTransactionManager);
    }
}
