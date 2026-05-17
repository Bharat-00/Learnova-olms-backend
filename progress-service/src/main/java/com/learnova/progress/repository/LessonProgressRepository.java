package com.learnova.progress.repository;

import com.learnova.progress.entity.LessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LessonProgressRepository extends JpaRepository<LessonProgress, Long> {

    boolean existsByUserEmailAndLessonId(String userEmail, Long lessonId);

    boolean existsByUserIdAndLessonId(Long userId, Long lessonId);

    long countByUserEmailAndCourseIdAndCompletedTrue(String userEmail, Long courseId);

    long countByUserIdAndCourseIdAndCompletedTrue(Long userId, Long courseId);

    List<LessonProgress> findByUserEmailAndCourseId(String userEmail, Long courseId);

    List<LessonProgress> findByUserIdAndCourseId(Long userId, Long courseId);

    Optional<LessonProgress> findByUserEmailAndLessonId(String userEmail, Long lessonId);

    Optional<LessonProgress> findByUserIdAndLessonId(Long userId, Long lessonId);
}
