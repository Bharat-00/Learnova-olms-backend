package com.learnova.enrollment.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learnova.enrollment.dto.EnrollmentResponse;
import com.learnova.enrollment.service.EnrollmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/courses/{courseId}")
    public EnrollmentResponse enrollInCourse(
            @PathVariable Long courseId,
            @RequestHeader("X-User-Email") String userEmail) {

        return enrollmentService.enrollInCourse(userEmail, courseId);
    }

    @GetMapping("/me")
    public List<EnrollmentResponse> getMyEnrollments(
            @RequestHeader("X-User-Email") String userEmail) {

        return enrollmentService.getUserEnrollments(userEmail);
    }

    @DeleteMapping("/courses/{courseId}")
public String unenrollFromCourse(
        @PathVariable Long courseId,
        @RequestHeader("X-User-Email") String userEmail) {

    enrollmentService.unenrollFromCourse(userEmail, courseId);

    return "Unenrolled from course successfully";
}
}