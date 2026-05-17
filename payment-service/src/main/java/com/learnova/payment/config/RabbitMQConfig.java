package com.learnova.payment.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange.payment}")
    private String paymentExchangeName;

    @Value("${rabbitmq.queue.payment-success}")
    private String paymentSuccessQueueName;

    @Value("${rabbitmq.routing-key.payment-success}")
    private String paymentSuccessRoutingKey;

    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange(paymentExchangeName, true, false);
    }

    @Bean
    public Queue paymentSuccessQueue() {
        return new Queue(paymentSuccessQueueName, true);
    }

    @Bean
    public Binding paymentSuccessBinding(Queue paymentSuccessQueue, TopicExchange paymentExchange) {
        return BindingBuilder
                .bind(paymentSuccessQueue)
                .to(paymentExchange)
                .with(paymentSuccessRoutingKey);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter messageConverter) {

        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}
