package com.learnova.payment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "enrollment-service")
public interface EnrollmentClient {

    @PostMapping("/api/v1/enrollments/courses/{courseId}")
    void enrollInCourse(
            @PathVariable("courseId") Long courseId,
            @RequestHeader("X-User-Email") String userEmail
    );
}