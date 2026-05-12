package com.learnova.notification.service;

import com.learnova.notification.dto.NotificationResponse;
import com.learnova.notification.entity.*;
import com.learnova.notification.event.PaymentSuccessEvent;
import com.learnova.notification.exception.ResourceNotFoundException;
import com.learnova.notification.repository.NotificationRepository;
import com.learnova.notification.security.SecurityContextUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SecurityContextUtil securityContextUtil;

    public void createPaymentSuccessNotification(PaymentSuccessEvent event) {

        Notification notification = Notification.builder()
                .userEmail(event.getUserEmail())
                .title("Payment Successful")
                .message("Your payment of ₹" + event.getAmount()
                        + " for course ID " + event.getCourseId()
                        + " was successful. Transaction ID: "
                        + event.getTransactionId())
                .type(NotificationType.PAYMENT_SUCCESS)
                .build();

        notificationRepository.save(notification);
    }

    public List<NotificationResponse> getMyNotifications(String userEmail) {

        String email = securityContextUtil.getUserEmail(userEmail);

        return notificationRepository.findByUserEmailOrderByCreatedAtDesc(email)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public NotificationResponse markAsRead(Long notificationId, String userEmail) {

        String email = securityContextUtil.getUserEmail(userEmail);

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));

        if (!notification.getUserEmail().equalsIgnoreCase(email)) {
            throw new ResourceNotFoundException("Notification not found for this user");
        }

        notification.setReadStatus(true);

        return mapToResponse(notificationRepository.save(notification));
    }

    private NotificationResponse mapToResponse(Notification notification) {

        return NotificationResponse.builder()
                .id(notification.getId())
                .userEmail(notification.getUserEmail())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType())
                .readStatus(notification.getReadStatus())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}