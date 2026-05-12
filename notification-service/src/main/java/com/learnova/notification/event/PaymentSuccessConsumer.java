package com.learnova.notification.event;

import com.learnova.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentSuccessConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = "${rabbitmq.queue.payment-success}")
    public void consumePaymentSuccessEvent(PaymentSuccessEvent event) {
        notificationService.createPaymentSuccessNotification(event);
    }
}