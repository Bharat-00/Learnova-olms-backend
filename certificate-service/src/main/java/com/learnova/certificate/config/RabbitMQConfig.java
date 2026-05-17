package com.learnova.certificate.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange}")
    private String exchangeName;

    @Value("${rabbitmq.certificate-queue}")
    private String certificateQueue;

    @Value("${rabbitmq.certificate-routing-key}")
    private String certificateRoutingKey;

    @Value("${rabbitmq.notification-queue}")
    private String notificationQueue;

    @Value("${rabbitmq.notification-routing-key}")
    private String notificationRoutingKey;

    @Bean
    public TopicExchange learnovaExchange() {
        return new TopicExchange(exchangeName, true, false);
    }

    @Bean
    public Queue certificateQueue() {
        return new Queue(certificateQueue, true);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(notificationQueue, true);
    }

    @Bean
    public Binding certificateBinding(
            @Qualifier("certificateQueue") Queue queue,
            TopicExchange learnovaExchange
    ) {
        return BindingBuilder
                .bind(queue)
                .to(learnovaExchange)
                .with(certificateRoutingKey);
    }

    @Bean
    public Binding notificationBinding(
            @Qualifier("notificationQueue") Queue queue,
            TopicExchange learnovaExchange
    ) {
        return BindingBuilder
                .bind(queue)
                .to(learnovaExchange)
                .with(notificationRoutingKey);
    }
}
