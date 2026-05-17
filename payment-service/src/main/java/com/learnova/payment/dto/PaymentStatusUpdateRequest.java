package com.learnova.payment.dto;

import com.learnova.payment.entity.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentStatusUpdateRequest {

    @NotNull(message = "Payment status is required")
    private PaymentStatus status;

    public static PaymentStatusUpdateRequest success() {
        return PaymentStatusUpdateRequest.builder()
                .status(PaymentStatus.SUCCESS)
                .build();
    }
}
