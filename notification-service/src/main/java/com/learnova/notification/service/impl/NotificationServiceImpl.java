package com.learnova.notification.service.impl;

import com.learnova.notification.dto.NotificationRequest;
import com.learnova.notification.entity.Notification;
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

        log.info("Creating notification for email={}", request.getUserEmail());

        Notification notification = Notification.builder()
                .userEmail(request.getUserEmail())
                .title(request.getTitle())
                .message(request.getMessage())
                .type(request.getType())
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
    public Notification markAsRead(Long id) {

        log.info("Marking notification as read with id={}", id);

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() ->
                        new NotificationException("Notification not found"));

        notification.setReadStatus(true);

        return notificationRepository.save(notification);
    }

    @Override
    public void deleteNotification(Long id) {

        log.info("Deleting notification with id={}", id);

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() ->
                        new NotificationException("Notification not found"));

        notificationRepository.delete(notification);
    }
}