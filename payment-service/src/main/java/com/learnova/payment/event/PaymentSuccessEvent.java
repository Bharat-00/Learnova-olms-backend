package com.learnova.payment.event;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentSuccessEvent {

    private Long paymentId;
    private Long courseId;
    private String userEmail;
    private Double amount;
    private String transactionId;
    private LocalDateTime paidAt;
}