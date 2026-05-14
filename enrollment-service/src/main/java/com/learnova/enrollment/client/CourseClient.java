package com.learnova.enrollment.client;

import com.learnova.enrollment.dto.CourseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "course-service", url = "http://localhost:8083")
public interface CourseClient {

    @GetMapping("/api/v1/courses/{id}")
    CourseResponse getCourseById(@PathVariable Long id);
}