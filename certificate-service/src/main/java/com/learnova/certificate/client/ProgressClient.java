package com.learnova.certificate.client;

import com.learnova.certificate.dto.ProgressResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "progress-service")
public interface ProgressClient {

    @GetMapping("/api/progress/user/{userId}/course/{courseId}")
    ProgressResponse getProgress(
            @PathVariable("userId") Long userId,
            @PathVariable("courseId") Long courseId
    );
}