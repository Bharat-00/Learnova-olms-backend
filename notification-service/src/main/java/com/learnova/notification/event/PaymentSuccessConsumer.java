package com.learnova.notification.event;

import com.learnova.notification.config.RabbitMQConfig;
import com.learnova.notification.dto.NotificationRequest;
import com.learnova.notification.entity.NotificationType;
import com.learnova.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentSuccessConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_SUCCESS_QUEUE)
    public void consume(PaymentSuccessEvent event) {
        log.info("Received payment success event for userEmail={}", event.getUserEmail());

        NotificationRequest request = new NotificationRequest();
        request.setUserEmail(event.getUserEmail());
        request.setTitle("Payment Successful");
        request.setMessage("Your payment for course purchase was completed successfully.");
        request.setType(NotificationType.SUCCESS);

        notificationService.createNotification(request);

        log.info("Payment success notification created for userEmail={}", event.getUserEmail());
    }
}
