package com.learnova.enrollment.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentResponse {

    private Long id;
    private String userEmail;
    private Long courseId;
    private String courseTitle;
    private LocalDateTime enrolledAt;
}