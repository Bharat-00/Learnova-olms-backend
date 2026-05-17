package com.learnova.payment.controller;

import com.learnova.payment.dto.PaymentRequest;
import com.learnova.payment.dto.PaymentResponse;
import com.learnova.payment.dto.PaymentStatusUpdateRequest;
import com.learnova.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public PaymentResponse createPayment(
            @Valid @RequestBody PaymentRequest request,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {

        return paymentService.createPayment(request, userEmail);
    }

    @PostMapping("/courses/{courseId}")
    public PaymentResponse createPaymentForCourse(
            @PathVariable("courseId") Long courseId,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {

        PaymentRequest request = PaymentRequest.builder()
                .courseId(courseId)
                .build();

        return paymentService.createPayment(request, userEmail);
    }

    @PatchMapping("/{paymentId}/status")
    public PaymentResponse updatePaymentStatus(
            @PathVariable("paymentId") Long paymentId,
            @Valid @RequestBody PaymentStatusUpdateRequest request) {

        return paymentService.updatePaymentStatus(paymentId, request);
    }

    @PostMapping("/{paymentId}/success")
    public PaymentResponse markPaymentSuccess(@PathVariable("paymentId") Long paymentId) {
        return paymentService.updatePaymentStatus(
                paymentId,
                PaymentStatusUpdateRequest.success()
        );
    }

    @GetMapping
    public List<PaymentResponse> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/me")
    public List<PaymentResponse> getMyPayments(
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {

        return paymentService.getMyPayments(userEmail);
    }

    @GetMapping("/{paymentId}")
    public PaymentResponse getPaymentById(
            @PathVariable("paymentId") Long paymentId,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {

        return paymentService.getPaymentById(paymentId, userEmail);
    }
}
