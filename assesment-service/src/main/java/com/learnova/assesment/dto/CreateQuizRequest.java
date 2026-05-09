package com.learnova.assesment.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateQuizRequest {

    @NotNull(message = "Course ID is required")
    private Long courseId;

    @NotBlank(message = "Quiz title is required")
    private String title;

    @NotBlank(message = "Quiz description is required")
    private String description;
}