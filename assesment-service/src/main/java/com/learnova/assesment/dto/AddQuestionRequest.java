package com.learnova.assesment.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddQuestionRequest {

    @NotBlank(message = "Question text is required")
    private String questionText;

    @NotBlank(message = "Option A is required")
    private String optionA;

    @NotBlank(message = "Option B is required")
    private String optionB;

    @NotBlank(message = "Option C is required")
    private String optionC;

    @NotBlank(message = "Option D is required")
    private String optionD;

    @Pattern(regexp = "A|B|C|D", message = "Correct option must be A, B, C or D")
    private String correctOption;

    @NotNull(message = "Marks are required")
    @Positive(message = "Marks must be greater than zero")
    private Integer marks;
}