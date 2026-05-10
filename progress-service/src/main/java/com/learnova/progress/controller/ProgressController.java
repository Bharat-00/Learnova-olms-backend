package com.learnova.progress.controller;

import com.learnova.progress.dto.CourseProgressResponse;
import com.learnova.progress.dto.LessonProgressResponse;
import com.learnova.progress.service.ProgressService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/progress")
@RequiredArgsConstructor
public class ProgressController {

    private final ProgressService progressService;

    @PostMapping("/courses/{courseId}/lessons/{lessonId}/complete")
    public LessonProgressResponse markLessonCompleted(
            @PathVariable Long courseId,
            @PathVariable Long lessonId,
            @RequestHeader("X-User-Email") String userEmail) {

        return progressService.markLessonCompleted(
                courseId,
                lessonId,
                userEmail
        );
    }

    @GetMapping("/courses/{courseId}")
    public CourseProgressResponse getCourseProgress(
            @PathVariable Long courseId,
            @RequestHeader("X-User-Email") String userEmail) {

        return progressService.getCourseProgress(
                courseId,
                userEmail
        );
    }

    @GetMapping("/courses/{courseId}/lessons")
    public List<LessonProgressResponse> getCompletedLessons(
            @PathVariable Long courseId,
            @RequestHeader("X-User-Email") String userEmail) {

        return progressService.getCompletedLessons(
                courseId,
                userEmail
        );
    }
}