package com.learnova.certificate.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange}")
    private String exchangeName;

    @Value("${rabbitmq.certificate-queue}")
    private String certificateQueue;

    @Value("${rabbitmq.notification-routing-key}")
    private String notificationRoutingKey;

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Queue certificateQueue() {
        return new Queue(certificateQueue);
    }

    @Bean
    public Binding binding(
            Queue certificateQueue,
            TopicExchange exchange
    ) {
        return BindingBuilder
                .bind(certificateQueue)
                .to(exchange)
                .with(notificationRoutingKey);
    }
}