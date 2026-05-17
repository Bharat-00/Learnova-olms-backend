package com.learnova.enrollment.client;

import com.learnova.enrollment.dto.CourseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "course-service")
public interface CourseClient {

    @GetMapping("/api/v1/courses/{id}")
    CourseResponse getCourseById(@PathVariable("id") Long id);
}