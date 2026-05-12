package com.learnova.notification.controller;

import com.learnova.notification.dto.NotificationResponse;
import com.learnova.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/me")
    public List<NotificationResponse> getMyNotifications(
            @RequestHeader("X-User-Email") String userEmail) {

        return notificationService.getMyNotifications(userEmail);
    }

    @PatchMapping("/{notificationId}/read")
    public NotificationResponse markAsRead(
            @PathVariable Long notificationId,
            @RequestHeader("X-User-Email") String userEmail) {

        return notificationService.markAsRead(notificationId, userEmail);
    }
}