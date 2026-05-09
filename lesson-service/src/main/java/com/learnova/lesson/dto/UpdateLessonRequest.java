package com.learnova.lesson.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateLessonRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    private String videoUrl;

    private String resourceUrl;

    @NotNull(message = "Lesson order is required")
    private Integer lessonOrder;
}