package com.example.eventbusservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;

import java.util.HashMap;

@Configuration
public class KafkaProducerConfiguration {

    @Bean
    public DefaultKafkaProducerFactory<String, Object> kafkaProducerFactory() {
        var config = new HashMap<String, Object>();

        return new DefaultKafkaProducerFactory<>(config);
    }
}
