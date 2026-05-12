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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
                .findByUserIdAndCourseId(
                        request.getUserId(),
                        request.getCourseId()
                )
                .orElse(null);

        if (existingCertificate != null) {
            return existingCertificate;
        }

        ProgressResponse progress = progressClient.getProgress(
                request.getUserId(),
                request.getCourseId()
        );

        if (progress == null || !progress.isCompleted()) {
            throw new CertificateException(
                    "Course is not completed yet"
            );
        }

        String certificateNumber =
                "LRN-CERT-" + UUID.randomUUID();

        Certificate certificate = Certificate.builder()
                .userId(request.getUserId())
                .courseId(request.getCourseId())
                .studentName(request.getStudentName())
                .courseTitle(request.getCourseTitle())
                .certificateNumber(certificateNumber)
                .issuedAt(LocalDateTime.now())
                .build();

        String filePath =
                pdfGeneratorService.generateCertificatePdf(certificate);

        certificate.setFileName(
                certificateNumber + ".pdf"
        );

        certificate.setFilePath(filePath);

        Certificate savedCertificate =
                certificateRepository.save(certificate);

        NotificationEvent event =
                NotificationEvent.builder()
                        .userId(savedCertificate.getUserId())
                        .title("Certificate Generated")
                        .message(
                                "Your certificate for course "
                                        + savedCertificate.getCourseTitle()
                                        + " has been generated successfully."
                        )
                        .build();

        rabbitMQPublisher.sendNotification(event);

        return savedCertificate;
    }

    @Override
    @Cacheable(value = "userCertificates", key = "#userId")
    public List<Certificate> getCertificatesByUserId(Long userId) {

        return certificateRepository.findByUserId(userId);
    }

    @Override
    @Cacheable(value = "certificate", key = "#certificateId")
    public Certificate getCertificateById(Long certificateId) {

        return certificateRepository.findById(certificateId)
                .orElseThrow(() ->
                        new CertificateException(
                                "Certificate not found"
                        )
                );
    }
}