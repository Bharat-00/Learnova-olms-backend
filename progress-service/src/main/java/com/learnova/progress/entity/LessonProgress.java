package com.learnova.progress.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "lesson_progress",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"userEmail", "lessonId"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;

    private Long courseId;

    private Long lessonId;

    private Boolean completed;

    private LocalDateTime completedAt;

    @PrePersist
    public void onCreate() {
        if (completed == null) {
            completed = true;
        }

        if (completedAt == null) {
            completedAt = LocalDateTime.now();
        }
    }
}