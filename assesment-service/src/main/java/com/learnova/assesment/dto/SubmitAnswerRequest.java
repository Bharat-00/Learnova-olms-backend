package com.learnova.assesment.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmitAnswerRequest {

    @NotNull(message = "Question ID is required")
    private Long questionId;

    @Pattern(regexp = "A|B|C|D", message = "Selected option must be A, B, C or D")
    private String selectedOption;
}