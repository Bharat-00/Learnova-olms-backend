package com.learnova.assesment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "quiz_attempts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long quizId;

    private String userEmail;

    private Integer totalQuestions;

    private Integer correctAnswers;

    private Integer totalMarks;

    private Integer scoredMarks;

    private LocalDateTime attemptedAt;

    @PrePersist
    public void onCreate() {
        attemptedAt = LocalDateTime.now();
    }
}