package com.learnova.enrollment.repository;

import com.learnova.enrollment.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByUserEmailAndCourseId(String userEmail, Long courseId);

    List<Enrollment> findByUserEmail(String userEmail);

    Optional<Enrollment> findByUserEmailAndCourseId(String userEmail, Long courseId);
}