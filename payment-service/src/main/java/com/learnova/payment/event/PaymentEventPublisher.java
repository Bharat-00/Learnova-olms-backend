package com.learnova.payment.event;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.payment}")
    private String paymentExchange;

    @Value("${rabbitmq.routing-key.payment-success}")
    private String paymentSuccessRoutingKey;

    public void publishPaymentSuccessEvent(PaymentSuccessEvent event) {
        rabbitTemplate.convertAndSend(
                paymentExchange,
                paymentSuccessRoutingKey,
                event
        );
    }
}