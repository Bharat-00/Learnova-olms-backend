package com.learnova.enrollment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;

    private Long courseId;

    private String courseTitle;

    private LocalDateTime enrolledAt;

    @PrePersist
    public void onCreate() {
        enrolledAt = LocalDateTime.now();
    }
}