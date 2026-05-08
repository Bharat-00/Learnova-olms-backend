package com.learnova.enrollment.service;

import com.learnova.enrollment.client.CourseClient;
import com.learnova.enrollment.dto.CourseResponse;
import com.learnova.enrollment.dto.EnrollmentResponse;
import com.learnova.enrollment.entity.Enrollment;
import com.learnova.enrollment.exception.BadRequestException;
import com.learnova.enrollment.exception.ResourceNotFoundException;
import com.learnova.enrollment.repository.EnrollmentRepository;
import com.learnova.enrollment.security.SecurityContextUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseClient courseClient;
    private final SecurityContextUtil securityContextUtil;

    public EnrollmentResponse enrollInCourse(String userEmail, Long courseId) {

        String email =
                securityContextUtil.getUserEmail(userEmail);

        boolean alreadyEnrolled =
                enrollmentRepository.existsByUserEmailAndCourseId(
                        email,
                        courseId
                );

        if (alreadyEnrolled) {
            throw new BadRequestException(
                    "User already enrolled in this course"
            );
        }

        CourseResponse course =
                courseClient.getCourseById(courseId);

        if (!course.getPublished()) {
            throw new BadRequestException(
                    "Course is not published yet"
            );
        }

        Enrollment enrollment = Enrollment.builder()
                .userEmail(email)
                .courseId(course.getId())
                .courseTitle(course.getTitle())
                .build();

        Enrollment savedEnrollment =
                enrollmentRepository.save(enrollment);

        return mapToResponse(savedEnrollment);
    }

    public List<EnrollmentResponse> getUserEnrollments(String userEmail) {

        String email =
                securityContextUtil.getUserEmail(userEmail);

        return enrollmentRepository.findByUserEmail(email)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void unenrollFromCourse(String userEmail, Long courseId) {

        String email =
                securityContextUtil.getUserEmail(userEmail);

        Enrollment enrollment =
                enrollmentRepository
                        .findByUserEmailAndCourseId(email, courseId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Enrollment not found"
                                ));

        enrollmentRepository.delete(enrollment);
    }

    private EnrollmentResponse mapToResponse(Enrollment enrollment) {

        return EnrollmentResponse.builder()
                .id(enrollment.getId())
                .userEmail(enrollment.getUserEmail())
                .courseId(enrollment.getCourseId())
                .courseTitle(enrollment.getCourseTitle())
                .enrolledAt(enrollment.getEnrolledAt())
                .build();
    }
}