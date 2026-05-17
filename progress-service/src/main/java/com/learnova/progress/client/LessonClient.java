package com.learnova.progress.client;

import com.learnova.progress.dto.LessonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "lesson-service")
public interface LessonClient {

    @GetMapping("/api/v1/lessons/course/{courseId}")
    List<LessonResponse> getLessonsByCourseId(@PathVariable("courseId") Long courseId);
}