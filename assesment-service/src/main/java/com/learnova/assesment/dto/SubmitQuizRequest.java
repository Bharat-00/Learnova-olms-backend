package com.learnova.assesment.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmitQuizRequest {

    @NotEmpty(message = "Answers are required")
    @Valid
    private List<SubmitAnswerRequest> answers;
}