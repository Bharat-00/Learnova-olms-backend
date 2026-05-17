package com.learnova.notification.messaging;

import com.learnova.notification.config.RabbitMQConfig;
import com.learnova.notification.dto.NotificationEvent;
import com.learnova.notification.entity.Notification;
import com.learnova.notification.entity.NotificationType;
import com.learnova.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationRepository notificationRepository;

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
    public void consume(NotificationEvent event) {
        log.info("Received notification event for userId={} with title={}", event.getUserId(), event.getTitle());

        Notification notification = Notification.builder()
                .userId(event.getUserId())
                .userEmail("bharat@gmail.com")
                .title(event.getTitle())
                .message(event.getMessage())
                .type(NotificationType.INFO)
                .readStatus(false)
                .createdAt(LocalDateTime.now())
                .build();

        Notification savedNotification = notificationRepository.save(notification);
        log.info("Notification stored successfully with id={}", savedNotification.getId());
    }
}
