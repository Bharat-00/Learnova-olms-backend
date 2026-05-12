package com.learnova.certificate.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learnova.certificate.entity.Certificate;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    List<Certificate> findByUserId(Long userId);

    Optional<Certificate> findByUserIdAndCourseId(Long userId, Long courseId);
}