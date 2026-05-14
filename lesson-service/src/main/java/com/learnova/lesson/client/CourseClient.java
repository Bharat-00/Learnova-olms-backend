package com.learnova.lesson.client;

import com.learnova.lesson.dto.CourseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "course-service",
        url = "http://localhost:8083"
)
public interface CourseClient {

    @GetMapping("/api/v1/courses/{id}")
    CourseResponse getCourseById(@PathVariable Long id);
}