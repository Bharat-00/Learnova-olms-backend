package com.learnova.progress.controller;

import com.learnova.progress.dto.CourseProgressResponse;
import com.learnova.progress.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LegacyProgressController {

    private final ProgressService progressService;

    @GetMapping("/api/progress/user/{userId}/course/{courseId}")
    public CourseProgressResponse getLegacyProgressForCertificate(
            @PathVariable("userId") Long userId,
            @PathVariable("courseId") Long courseId) {
        return progressService.getCourseProgressByUserId(userId, courseId);
    }
}
