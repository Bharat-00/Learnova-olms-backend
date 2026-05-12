package com.learnova.certificate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CertificateRequest {

    @NotNull
    private Long userId;

    @NotNull
    private Long courseId;

    @NotBlank
    private String studentName;

    @NotBlank
    private String courseTitle;
}