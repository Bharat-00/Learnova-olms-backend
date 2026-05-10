package com.learnova.progress.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "course_progress",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"userEmail", "courseId"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;

    private Long courseId;

    private Integer totalLessons;

    private Integer completedLessons;

    private Double completionPercentage;

    private Boolean certificateEligible;

    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}