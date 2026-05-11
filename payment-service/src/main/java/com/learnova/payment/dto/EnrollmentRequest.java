package com.learnova.payment.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentRequest {

    private Long courseId;
    private String userEmail;
}