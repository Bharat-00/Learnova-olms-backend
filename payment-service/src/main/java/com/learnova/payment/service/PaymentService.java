package com.learnova.payment.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.learnova.payment.client.CourseClient;
import com.learnova.payment.client.EnrollmentClient;
import com.learnova.payment.dto.CourseResponse;
import com.learnova.payment.dto.PaymentRequest;
import com.learnova.payment.dto.PaymentResponse;
import com.learnova.payment.dto.PaymentStatusUpdateRequest;
import com.learnova.payment.entity.Payment;
import com.learnova.payment.entity.PaymentStatus;
import com.learnova.payment.event.PaymentEventPublisher;
import com.learnova.payment.event.PaymentSuccessEvent;
import com.learnova.payment.exception.BadRequestException;
import com.learnova.payment.exception.ResourceNotFoundException;
import com.learnova.payment.repository.PaymentRepository;
import com.learnova.payment.security.SecurityContextUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final CourseClient courseClient;
    private final EnrollmentClient enrollmentClient;
    private final PaymentEventPublisher paymentEventPublisher;
    private final SecurityContextUtil securityContextUtil;

    public PaymentResponse createPayment(PaymentRequest request, String userEmail) {

        String email = securityContextUtil.getUserEmail(userEmail);

        CourseResponse course = courseClient.getCourseById(request.getCourseId());

        if (Boolean.FALSE.equals(course.getPublished())) {
            throw new BadRequestException("Payment cannot be created for unpublished course");
        }

        boolean alreadyPaid = paymentRepository
                .findByUserEmailAndCourseIdAndStatus(
                        email,
                        request.getCourseId(),
                        PaymentStatus.SUCCESS
                )
                .isPresent();

        if (alreadyPaid) {
            throw new BadRequestException("Payment already completed for this course");
        }

        Payment payment = Payment.builder()
                .userEmail(email)
                .courseId(course.getId())
                .amount(course.getPrice() == null ? 0.0 : course.getPrice())
                .transactionId("TXN-" + UUID.randomUUID())
                .status(PaymentStatus.PENDING)
                .build();

        return mapToResponse(paymentRepository.save(payment));
    }

    public PaymentResponse updatePaymentStatus(Long paymentId, PaymentStatusUpdateRequest request) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        PaymentStatus oldStatus = payment.getStatus();

        payment.setStatus(request.getStatus());

        Payment savedPayment = paymentRepository.save(payment);

        if (oldStatus != PaymentStatus.SUCCESS && savedPayment.getStatus() == PaymentStatus.SUCCESS) {
            createEnrollmentAfterPaymentSuccess(savedPayment);
            publishPaymentSuccessEvent(savedPayment);
        }

        return mapToResponse(savedPayment);
    }

    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<PaymentResponse> getMyPayments(String userEmail) {

        String email = securityContextUtil.getUserEmail(userEmail);

        return paymentRepository.findByUserEmail(email)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public PaymentResponse getPaymentById(Long paymentId, String userEmail) {

        String email = securityContextUtil.getUserEmail(userEmail);

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        if (!payment.getUserEmail().equalsIgnoreCase(email)) {
            throw new BadRequestException("You cannot access this payment");
        }

        return mapToResponse(payment);
    }

    private void createEnrollmentAfterPaymentSuccess(Payment payment) {
        enrollmentClient.enrollInCourse(
                payment.getCourseId(),
                payment.getUserEmail()
        );
    }

    private void publishPaymentSuccessEvent(Payment payment) {

        PaymentSuccessEvent event = PaymentSuccessEvent.builder()
                .paymentId(payment.getId())
                .courseId(payment.getCourseId())
                .userEmail(payment.getUserEmail())
                .amount(payment.getAmount())
                .transactionId(payment.getTransactionId())
                .paidAt(LocalDateTime.now())
                .build();

        paymentEventPublisher.publishPaymentSuccessEvent(event);
    }

    private PaymentResponse mapToResponse(Payment payment) {

        return PaymentResponse.builder()
                .id(payment.getId())
                .userEmail(payment.getUserEmail())
                .courseId(payment.getCourseId())
                .amount(payment.getAmount())
                .transactionId(payment.getTransactionId())
                .status(payment.getStatus())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}