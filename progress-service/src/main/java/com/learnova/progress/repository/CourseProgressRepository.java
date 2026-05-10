package com.learnova.progress.repository;

import com.learnova.progress.entity.CourseProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseProgressRepository extends JpaRepository<CourseProgress, Long> {

    Optional<CourseProgress> findByUserEmailAndCourseId(String userEmail, Long courseId);
}