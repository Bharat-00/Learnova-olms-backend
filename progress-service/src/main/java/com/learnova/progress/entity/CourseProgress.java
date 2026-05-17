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

    private Long userId;

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
        if (totalLessons == null) {
            totalLessons = 0;
        }
        if (completedLessons == null) {
            completedLessons = 0;
        }
        if (completionPercentage == null) {
            completionPercentage = 0.0;
        }
        if (certificateEligible == null) {
            certificateEligible = false;
        }
        updatedAt = LocalDateTime.now();
    }
}
