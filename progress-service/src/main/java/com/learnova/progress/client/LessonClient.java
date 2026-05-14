package com.learnova.progress.client;

import com.learnova.progress.dto.LessonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "lesson-service",
        url = "http://localhost:8085"
)
public interface LessonClient {

    @GetMapping("/api/v1/lessons/course/{courseId}")
    List<LessonResponse> getLessonsByCourseId(@PathVariable Long courseId);
}