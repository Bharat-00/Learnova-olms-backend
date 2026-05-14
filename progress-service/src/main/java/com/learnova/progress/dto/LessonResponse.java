package com.learnova.progress.dto;

import lombok.*;

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
}