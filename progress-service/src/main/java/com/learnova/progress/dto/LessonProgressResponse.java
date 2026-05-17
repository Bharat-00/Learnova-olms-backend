package com.learnova.progress.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonProgressResponse {

    private Long id;
    private Long userId;
    private String userEmail;
    private Long courseId;
    private Long lessonId;
    private Boolean completed;
    private LocalDateTime completedAt;
}
