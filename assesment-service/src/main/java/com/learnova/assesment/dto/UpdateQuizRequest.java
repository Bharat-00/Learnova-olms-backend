package com.learnova.assesment.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateQuizRequest {

    @NotBlank(message = "Quiz title is required")
    private String title;

    @NotBlank(message = "Quiz description is required")
    private String description;

    @NotNull(message = "Active status is required")
    private Boolean active;
}