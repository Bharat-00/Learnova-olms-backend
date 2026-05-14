package com.learnova.payment.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learnova.payment.dto.PaymentRequest;
import com.learnova.payment.dto.PaymentResponse;
import com.learnova.payment.dto.PaymentStatusUpdateRequest;
import com.learnova.payment.service.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public PaymentResponse createPayment(
            @Valid @RequestBody PaymentRequest request,
            @RequestHeader("X-User-Email") String userEmail) {

        return paymentService.createPayment(request, userEmail);
    }

    @PatchMapping("/{paymentId}/status")
    public PaymentResponse updatePaymentStatus(
            @PathVariable Long paymentId,
            @Valid @RequestBody PaymentStatusUpdateRequest request) {

        return paymentService.updatePaymentStatus(paymentId, request);
    }

    @GetMapping("/me")
    public List<PaymentResponse> getMyPayments(
            @RequestHeader("X-User-Email") String userEmail) {

        return paymentService.getMyPayments(userEmail);
    }

    @GetMapping("/{paymentId}")
    public PaymentResponse getPaymentById(
            @PathVariable Long paymentId,
            @RequestHeader("X-User-Email") String userEmail) {

        return paymentService.getPaymentById(paymentId, userEmail);
    }
}