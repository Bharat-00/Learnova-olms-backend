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
@RequestMapping({"/api/v1/notifications", "/api/notifications"})
@Tag(name = "Notification Controller", description = "APIs for notification management")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    @Operation(summary = "Create notification")
    public ResponseEntity<ApiResponse<Notification>> createNotification(@Valid @RequestBody NotificationRequest request) {
        log.info("Creating notification for email={}", request.getUserEmail());
        Notification notification = notificationService.createNotification(request);
        return ResponseEntity.status(201).body(success("Notification created successfully", notification));
    }

    @GetMapping
    @Operation(summary = "Get all notifications")
    public ResponseEntity<List<Notification>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user notifications")
    public ResponseEntity<List<Notification>> getMyNotifications(
            @RequestHeader(value = "X-User-Email", required = false) String userEmail
    ) {
        String email = userEmail == null || userEmail.isBlank() ? "bharat@gmail.com" : userEmail;
        return ResponseEntity.ok(notificationService.getNotificationsByUserEmail(email));
    }

    @GetMapping("/user/{email}")
    @Operation(summary = "Get notifications by user email")
    public ResponseEntity<ApiResponse<List<Notification>>> getNotificationsByUserEmail(@PathVariable("email") String email) {
        List<Notification> notifications = notificationService.getNotificationsByUserEmail(email);
        return ResponseEntity.ok(success("User notifications fetched successfully", notifications));
    }

    @GetMapping("/users/{userId}")
    @Operation(summary = "Get notifications by user id")
    public ResponseEntity<ApiResponse<List<Notification>>> getNotificationsByUserId(@PathVariable("userId") Long userId) {
        List<Notification> notifications = notificationService.getNotificationsByUserId(userId);
        return ResponseEntity.ok(success("User notifications fetched successfully", notifications));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get notification by id")
    public ResponseEntity<Notification> getNotificationById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(notificationService.getAllNotifications().stream()
                .filter(notification -> id.equals(notification.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Notification not found")));
    }

    @PutMapping("/{id}/read")
    @Operation(summary = "Mark notification as read")
    public ResponseEntity<ApiResponse<Notification>> markAsRead(@PathVariable("id") Long id) {
        Notification notification = notificationService.markAsRead(id);
        return ResponseEntity.ok(success("Notification marked as read", notification));
    }

    @PatchMapping("/{id}/read")
    @Operation(summary = "Mark notification as read")
    public ResponseEntity<ApiResponse<Notification>> patchMarkAsRead(@PathVariable("id") Long id) {
        Notification notification = notificationService.markAsRead(id);
        return ResponseEntity.ok(success("Notification marked as read", notification));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete notification")
    public ResponseEntity<ApiResponse<String>> deleteNotification(@PathVariable("id") Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok(success("Notification deleted successfully", "Deleted"));
    }

    private <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
