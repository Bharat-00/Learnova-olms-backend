package com.learnova.certificate.controller;

import java.io.File;
import java.util.List;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learnova.certificate.dto.CertificateRequest;
import com.learnova.certificate.entity.Certificate;
import com.learnova.certificate.service.CertificateService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/certificates")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;

    @PostMapping("/generate")
    public ResponseEntity<Certificate> generateCertificate(
            @Valid @RequestBody CertificateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(certificateService.generateCertificate(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Certificate>> getCertificatesByUserId(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(certificateService.getCertificatesByUserId(userId));
    }

    @GetMapping("/download/{certificateId}")
    public ResponseEntity<Resource> downloadCertificate(
            @PathVariable Long certificateId
    ) {
        Certificate certificate = certificateService.getCertificateById(certificateId);

        File file = new File(certificate.getFilePath());
        Resource resource = new FileSystemResource(file);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + certificate.getFileName() + "\""
                )
                .body(resource);
    }
}