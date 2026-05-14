package com.learnova.payment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learnova.payment.entity.Payment;
import com.learnova.payment.entity.PaymentStatus;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByUserEmail(String userEmail);

    Optional<Payment> findByUserEmailAndCourseIdAndStatus(
            String userEmail,
            Long courseId,
            PaymentStatus status
    );
}