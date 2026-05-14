package com.learnova.notification.dto;

import lombok.Data;

@Data
public class NotificationEvent {

    private Long userId;

    private String title;

    private String message;
}