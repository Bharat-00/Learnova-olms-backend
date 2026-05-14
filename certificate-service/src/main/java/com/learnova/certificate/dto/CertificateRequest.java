package com.learnova.certificate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CertificateRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Course ID is required")
    private Long courseId;

    @NotBlank(message = "Student name is required")
    private String studentName;

    @NotBlank(message = "Course title is required")
    private String courseTitle;
}