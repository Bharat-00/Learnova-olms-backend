package com.learnova.payment.dto;

import com.learnova.payment.entity.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentStatusUpdateRequest {

    @NotNull(message = "Payment status is required")
    private PaymentStatus status;
}