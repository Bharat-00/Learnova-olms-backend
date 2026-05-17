package com.learnova.progress.controller;

import com.learnova.progress.dto.CourseProgressResponse;
import com.learnova.progress.dto.LessonProgressResponse;
import com.learnova.progress.dto.ProgressUpdateRequest;
import com.learnova.progress.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/progress")
@RequiredArgsConstructor
public class ProgressController {

    private final ProgressService progressService;

    @GetMapping
    public List<CourseProgressResponse> getMyProgress(
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {
        return progressService.getMyProgress(userEmail);
    }

    @PostMapping
    public LessonProgressResponse updateProgress(
            @RequestBody ProgressUpdateRequest request,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {
        return progressService.updateProgress(request, userEmail);
    }

    @PutMapping
    public LessonProgressResponse updateProgressWithPut(
            @RequestBody ProgressUpdateRequest request,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {
        return progressService.updateProgress(request, userEmail);
    }

    @PostMapping("/courses/{courseId}/lessons/{lessonId}/complete")
    public LessonProgressResponse markLessonCompleted(
            @PathVariable("courseId") Long courseId,
            @PathVariable("lessonId") Long lessonId,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {
        return progressService.markLessonCompleted(courseId, lessonId, userEmail);
    }

    @PostMapping("/user/{userId}/course/{courseId}/lesson/{lessonId}/complete")
    public LessonProgressResponse markLessonCompletedByUserId(
            @PathVariable("userId") Long userId,
            @PathVariable("courseId") Long courseId,
            @PathVariable("lessonId") Long lessonId) {
        return progressService.markLessonCompleted(userId, courseId, lessonId, null);
    }

    @GetMapping("/courses/{courseId}")
    public CourseProgressResponse getCourseProgress(
            @PathVariable("courseId") Long courseId,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {
        return progressService.getCourseProgress(courseId, userEmail);
    }

    @GetMapping("/user/{userId}/course/{courseId}")
    public CourseProgressResponse getCourseProgressByUserId(
            @PathVariable("userId") Long userId,
            @PathVariable("courseId") Long courseId) {
        return progressService.getCourseProgressByUserId(userId, courseId);
    }

    @GetMapping("/courses/{courseId}/lessons")
    public List<LessonProgressResponse> getCompletedLessons(
            @PathVariable("courseId") Long courseId,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {
        return progressService.getCompletedLessons(courseId, userEmail);
    }
}
