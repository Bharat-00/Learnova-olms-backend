package com.learnova.enrollment.controller;

import com.learnova.enrollment.dto.EnrollmentResponse;
import com.learnova.enrollment.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/courses/{courseId}")
    public EnrollmentResponse enrollInCourse(
            @PathVariable("courseId") Long courseId,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {
        return enrollmentService.enrollInCourse(userEmail, courseId);
    }

    @GetMapping("/me")
    public List<EnrollmentResponse> getMyEnrollments(
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {
        return enrollmentService.getUserEnrollments(userEmail);
    }

    @GetMapping("/users/{userEmail}")
    public List<EnrollmentResponse> getUserEnrollmentsByEmail(@PathVariable("userEmail") String userEmail) {
        return enrollmentService.getUserEnrollments(userEmail);
    }

    @GetMapping("/courses/{courseId}")
    public List<EnrollmentResponse> getCourseEnrollments(@PathVariable("courseId") Long courseId) {
        return enrollmentService.getCourseEnrollments(courseId);
    }

    @GetMapping("/check/courses/{courseId}")
    public Map<String, Boolean> checkMyEnrollment(
            @PathVariable("courseId") Long courseId,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {
        return Map.of("enrolled", enrollmentService.isUserEnrolled(userEmail, courseId));
    }

    @DeleteMapping("/courses/{courseId}")
    public ResponseEntity<Map<String, String>> unenrollFromCourse(
            @PathVariable("courseId") Long courseId,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {
        enrollmentService.unenrollFromCourse(userEmail, courseId);
        return ResponseEntity.ok(Map.of("message", "Unenrolled from course successfully"));
    }
}
