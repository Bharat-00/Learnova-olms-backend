package com.learnova.notification.service.impl;

import com.learnova.notification.dto.NotificationRequest;
import com.learnova.notification.entity.Notification;
import com.learnova.notification.entity.NotificationType;
import com.learnova.notification.exception.NotificationException;
import com.learnova.notification.repository.NotificationRepository;
import com.learnova.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public Notification createNotification(NotificationRequest request) {
        String email = request.getUserEmail();
        if (email == null || email.isBlank()) {
            email = "bharat@gmail.com";
        }

        NotificationType type = request.getType() == null
                ? NotificationType.INFO
                : request.getType();

        log.info("Creating notification for email={}", email);

        Notification notification = Notification.builder()
                .userId(request.getUserId())
                .userEmail(email)
                .title(request.getTitle())
                .message(request.getMessage())
                .type(type)
                .readStatus(false)
                .build();

        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getAllNotifications() {
        log.info("Fetching all notifications");
        return notificationRepository.findAll();
    }

    @Override
    public List<Notification> getNotificationsByUserEmail(String email) {
        log.info("Fetching notifications for email={}", email);
        return notificationRepository.findByUserEmail(email);
    }

    @Override
    public List<Notification> getNotificationsByUserId(Long userId) {
        log.info("Fetching notifications for userId={}", userId);
        return notificationRepository.findByUserId(userId);
    }

    @Override
    public Notification markAsRead(Long id) {
        log.info("Marking notification as read with id={}", id);

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationException("Notification not found"));

        notification.setReadStatus(true);
        return notificationRepository.save(notification);
    }

    @Override
    public void deleteNotification(Long id) {
        log.info("Deleting notification with id={}", id);

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationException("Notification not found"));

        notificationRepository.delete(notification);
    }
}
