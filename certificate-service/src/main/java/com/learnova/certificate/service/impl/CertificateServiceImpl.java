package com.learnova.certificate.service.impl;

import com.learnova.certificate.client.ProgressClient;
import com.learnova.certificate.dto.CertificateRequest;
import com.learnova.certificate.dto.NotificationEvent;
import com.learnova.certificate.dto.ProgressResponse;
import com.learnova.certificate.entity.Certificate;
import com.learnova.certificate.exception.CertificateException;
import com.learnova.certificate.repository.CertificateRepository;
import com.learnova.certificate.service.CertificateService;
import com.learnova.certificate.service.PdfGeneratorService;
import com.learnova.certificate.service.RabbitMQPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;
    private final PdfGeneratorService pdfGeneratorService;
    private final ProgressClient progressClient;
    private final RabbitMQPublisher rabbitMQPublisher;

    @Override
    @CacheEvict(value = "userCertificates", key = "#request.userId")
    public Certificate generateCertificate(CertificateRequest request) {
        Certificate existingCertificate = certificateRepository
                .findByUserIdAndCourseId(request.getUserId(), request.getCourseId())
                .orElse(null);

        if (existingCertificate != null) {
            return existingCertificate;
        }

        validateProgress(request.getUserId(), request.getCourseId());

        String certificateNumber = "LRN-CERT-" + UUID.randomUUID();

        Certificate certificate = Certificate.builder()
                .userId(request.getUserId())
                .courseId(request.getCourseId())
                .studentName(request.getStudentName())
                .courseTitle(request.getCourseTitle())
                .certificateNumber(certificateNumber)
                .issuedAt(LocalDateTime.now())
                .build();

        String filePath = pdfGeneratorService.generateCertificatePdf(certificate);

        certificate.setFileName(certificateNumber + ".pdf");
        certificate.setFilePath(filePath);

        Certificate savedCertificate = certificateRepository.save(certificate);

        NotificationEvent event = NotificationEvent.builder()
                .userId(savedCertificate.getUserId())
                .title("Certificate Generated")
                .message("Your certificate for course " + savedCertificate.getCourseTitle()
                        + " has been generated successfully.")
                .build();

        try {
            rabbitMQPublisher.sendNotification(event);
        } catch (Exception ex) {
            log.warn("Certificate generated but notification publish failed: {}", ex.getMessage());
        }

        return savedCertificate;
    }

    private void validateProgress(Long userId, Long courseId) {
        try {
            ProgressResponse progress = progressClient.getProgress(userId, courseId);

            if (progress == null || !progress.isCompleted()) {
                throw new CertificateException("Course is not completed yet");
            }
        } catch (CertificateException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new CertificateException("Unable to verify course progress right now");
        }
    }

    @Override
    @Cacheable(value = "userCertificates", key = "#userId")
    public List<Certificate> getCertificatesByUserId(Long userId) {
        return certificateRepository.findByUserId(userId);
    }

    @Override
    public List<Certificate> getAllCertificates() {
        return certificateRepository.findAll();
    }

    @Override
    @Cacheable(value = "certificate", key = "#certificateId")
    public Certificate getCertificateById(Long certificateId) {
        return certificateRepository.findById(certificateId)
                .orElseThrow(() -> new CertificateException("Certificate not found"));
    }
}
