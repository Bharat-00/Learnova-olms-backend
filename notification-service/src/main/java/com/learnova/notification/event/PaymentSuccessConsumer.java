package com.learnova.notification.event;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.learnova.notification.dto.NotificationRequest;
import com.learnova.notification.entity.NotificationType;
import com.learnova.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentSuccessConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = "payment.success.queue")
    public void consume(PaymentSuccessEvent event) {

        log.info(
                "Received payment success event for userEmail={}",
                event.getUserEmail()
        );

        NotificationRequest request = new NotificationRequest();

        request.setUserEmail(event.getUserEmail());

        request.setTitle("Payment Successful");

        request.setMessage(
                "Your payment for course purchase was completed successfully."
        );

        request.setType(NotificationType.SUCCESS);

        notificationService.createNotification(request);

        log.info(
                "Payment success notification created for userEmail={}",
                event.getUserEmail()
        );
    }
}