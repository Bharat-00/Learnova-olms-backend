package com.learnova.certificate.service;

import com.learnova.certificate.dto.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitMQPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.notification-routing-key}")
    private String routingKey;

    public void sendNotification(NotificationEvent event) {
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
        log.info("Certificate notification event published for userId={}", event.getUserId());
    }
}
