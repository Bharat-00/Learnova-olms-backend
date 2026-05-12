package com.learnova.notification.dto;

import com.learnova.notification.entity.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {

    private Long id;
    private String userEmail;
    private String title;
    private String message;
    private NotificationType type;
    private Boolean readStatus;
    private LocalDateTime createdAt;
}