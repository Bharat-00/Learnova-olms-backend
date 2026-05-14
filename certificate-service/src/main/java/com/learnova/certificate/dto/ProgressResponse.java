package com.learnova.certificate.dto;

import lombok.Data;

@Data
public class ProgressResponse {

    private Long userId;

    private Long courseId;

    private double completionPercentage;

    private boolean completed;
}