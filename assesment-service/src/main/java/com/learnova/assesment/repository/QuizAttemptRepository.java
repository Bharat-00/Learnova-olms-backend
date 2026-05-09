package com.learnova.assesment.repository;

import com.learnova.assesment.entity.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {

    List<QuizAttempt> findByUserEmail(String userEmail);

    List<QuizAttempt> findByQuizId(Long quizId);
}