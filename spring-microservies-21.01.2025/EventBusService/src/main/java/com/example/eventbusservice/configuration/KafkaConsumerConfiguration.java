package com.example.eventbusservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;

@Configuration
public class KafkaConsumerConfiguration {

    @Bean
    public DefaultKafkaConsumerFactory<String, Object> kafkaConsumerFactory() {
        var config = new HashMap<String, Object>();
        return new DefaultKafkaConsumerFactory<>(config);
    }
}
