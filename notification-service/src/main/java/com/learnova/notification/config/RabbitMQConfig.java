package com.learnova.notification.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "learnova.exchange";
    public static final String NOTIFICATION_QUEUE = "notification.queue";
    public static final String NOTIFICATION_ROUTING_KEY = "notification.created";

    @Bean
    public TopicExchange learnovaExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE);
    }

    @Bean
    public Binding notificationBinding(
            Queue notificationQueue,
            TopicExchange learnovaExchange
    ) {
        return BindingBuilder
                .bind(notificationQueue)
                .to(learnovaExchange)
                .with(NOTIFICATION_ROUTING_KEY);
    }
}