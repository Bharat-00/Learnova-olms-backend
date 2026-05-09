package com.learnova.lesson.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonResponse {

    private Long id;
    private Long courseId;
    private String title;
    private String content;
    private String videoUrl;
    private String resourceUrl;
    private Integer lessonOrder;
    private String instructorEmail;
    private LocalDateTime createdAt;
}