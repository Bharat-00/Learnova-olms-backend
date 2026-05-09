package com.learnova.assesment.repository;

import com.learnova.assesment.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    List<Quiz> findByCourseId(Long courseId);

    List<Quiz> findByCourseIdAndActiveTrue(Long courseId);
}