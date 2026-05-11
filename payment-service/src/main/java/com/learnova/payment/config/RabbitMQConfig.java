package com.learnova.payment.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange.payment}")
    private String paymentExchange;

    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange(paymentExchange);
    }
}