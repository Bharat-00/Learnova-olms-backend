package com.learnova.notification.service;

import java.util.List;

import com.learnova.notification.dto.NotificationRequest;
import com.learnova.notification.entity.Notification;

public interface NotificationService {

    Notification createNotification(NotificationRequest request);

    List<Notification> getAllNotifications();

    List<Notification> getNotificationsByUserEmail(String email);

    Notification markAsRead(Long id);

    void deleteNotification(Long id);
}