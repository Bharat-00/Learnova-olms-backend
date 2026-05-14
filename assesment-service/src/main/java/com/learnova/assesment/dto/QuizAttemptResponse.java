package com.learnova.assesment.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizAttemptResponse {

    private Long id;
    private Long quizId;
    private String userEmail;
    private Integer totalQuestions;
    private Integer correctAnswers;
    private Integer totalMarks;
    private Integer scoredMarks;
    private LocalDateTime attemptedAt;
}