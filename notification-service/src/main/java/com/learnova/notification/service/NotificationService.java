package com.learnova.notification.service;

import com.learnova.notification.dto.NotificationRequest;
import com.learnova.notification.entity.Notification;

import java.util.List;

public interface NotificationService {

    Notification createNotification(NotificationRequest request);

    List<Notification> getAllNotifications();

    List<Notification> getNotificationsByUserEmail(String email);

    List<Notification> getNotificationsByUserId(Long userId);

    Notification markAsRead(Long id);

    void deleteNotification(Long id);
}
