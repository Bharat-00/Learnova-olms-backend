package com.learnova.notification.controller;

import com.learnova.notification.dto.ApiResponse;
import com.learnova.notification.dto.NotificationRequest;
import com.learnova.notification.entity.Notification;
import com.learnova.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
@Tag(name = "Notification Controller", description = "APIs for notification management")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    @Operation(summary = "Create notification")
    public ResponseEntity<ApiResponse<Notification>> createNotification(
            @Valid @RequestBody NotificationRequest request
    ) {

        log.info("Creating notification for email={}", request.getUserEmail());

        Notification notification =
                notificationService.createNotification(request);

        return ResponseEntity.status(201)
                .body(
                        ApiResponse.<Notification>builder()
                                .success(true)
                                .message("Notification created successfully")
                                .data(notification)
                                .timestamp(LocalDateTime.now())
                                .build()
                );
    }

    @GetMapping
    @Operation(summary = "Get all notifications")
    public ResponseEntity<ApiResponse<List<Notification>>> getAllNotifications() {

        List<Notification> notifications =
                notificationService.getAllNotifications();

        return ResponseEntity.ok(
                ApiResponse.<List<Notification>>builder()
                        .success(true)
                        .message("Notifications fetched successfully")
                        .data(notifications)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/user/{email}")
    @Operation(summary = "Get notifications by user email")
    public ResponseEntity<ApiResponse<List<Notification>>> getNotificationsByUserEmail(
            @PathVariable String email
    ) {

        List<Notification> notifications =
                notificationService.getNotificationsByUserEmail(email);

        return ResponseEntity.ok(
                ApiResponse.<List<Notification>>builder()
                        .success(true)
                        .message("User notifications fetched successfully")
                        .data(notifications)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PutMapping("/{id}/read")
    @Operation(summary = "Mark notification as read")
    public ResponseEntity<ApiResponse<Notification>> markAsRead(
            @PathVariable Long id
    ) {

        Notification notification =
                notificationService.markAsRead(id);

        return ResponseEntity.ok(
                ApiResponse.<Notification>builder()
                        .success(true)
                        .message("Notification marked as read")
                        .data(notification)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete notification")
    public ResponseEntity<ApiResponse<String>> deleteNotification(
            @PathVariable Long id
    ) {

        notificationService.deleteNotification(id);

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Notification deleted successfully")
                        .data("Deleted")
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}