package com.learnova.assesment.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizResponse {

    private Long id;
    private Long courseId;
    private String title;
    private String description;
    private String instructorEmail;
    private Boolean active;
    private LocalDateTime createdAt;
    private List<QuestionResponse> questions;
}