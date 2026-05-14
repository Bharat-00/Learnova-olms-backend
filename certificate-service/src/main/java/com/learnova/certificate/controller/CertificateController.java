package com.learnova.certificate.controller;

import com.learnova.certificate.dto.ApiResponse;
import com.learnova.certificate.dto.CertificateRequest;
import com.learnova.certificate.entity.Certificate;
import com.learnova.certificate.service.CertificateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/certificates")
@Tag(name = "Certificate Controller", description = "APIs for certificate management")
public class CertificateController {

    private final CertificateService certificateService;

    @PostMapping("/generate")
    @Operation(summary = "Generate course completion certificate")
    public ResponseEntity<ApiResponse<Certificate>> generateCertificate(
            @Valid @RequestBody CertificateRequest request
    ) {

        log.info("Generating certificate for userId={} and courseId={}",
                request.getUserId(),
                request.getCourseId());

        Certificate certificate =
                certificateService.generateCertificate(request);

        return ResponseEntity.status(201)
                .body(
                        ApiResponse.<Certificate>builder()
                                .success(true)
                                .message("Certificate generated successfully")
                                .data(certificate)
                                .timestamp(LocalDateTime.now())
                                .build()
                );
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get certificates by user ID")
    public ResponseEntity<ApiResponse<List<Certificate>>> getCertificatesByUserId(
            @PathVariable Long userId
    ) {

        List<Certificate> certificates =
                certificateService.getCertificatesByUserId(userId);

        return ResponseEntity.ok(
                ApiResponse.<List<Certificate>>builder()
                        .success(true)
                        .message("Certificates fetched successfully")
                        .data(certificates)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/download/{certificateId}")
    @Operation(summary = "Download certificate PDF")
    public ResponseEntity<Resource> downloadCertificate(
            @PathVariable Long certificateId
    ) throws Exception {

        Certificate certificate =
                certificateService.getCertificateById(certificateId);

        Path filePath = Path.of(certificate.getFilePath());

        Resource resource =
                new org.springframework.core.io.UrlResource(filePath.toUri());

        String contentType =
                Files.probeContentType(filePath);

        if (contentType == null) {
            contentType = "application/pdf";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                certificate.getFileName() + "\""
                )
                .body(resource);
    }
}