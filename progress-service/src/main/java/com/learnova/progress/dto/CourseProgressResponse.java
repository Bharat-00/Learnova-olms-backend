package com.learnova.progress.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseProgressResponse {

    private Long id;
    private String userEmail;
    private Long courseId;
    private Integer totalLessons;
    private Integer completedLessons;
    private Double completionPercentage;
    private Boolean certificateEligible;
    private LocalDateTime updatedAt;
}