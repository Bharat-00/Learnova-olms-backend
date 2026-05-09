package com.learnova.lesson.controller;

import com.learnova.lesson.dto.*;
import com.learnova.lesson.service.LessonService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @PostMapping
    public LessonResponse createLesson(
            @Valid @RequestBody CreateLessonRequest request,
            @RequestHeader("X-User-Email") String userEmail,
            @RequestHeader("X-User-Role") String role) {

        return lessonService.createLesson(
                request,
                userEmail,
                role
        );
    }

    @GetMapping("/course/{courseId}")
    public List<LessonResponse> getCourseLessons(
            @PathVariable Long courseId) {

        return lessonService.getCourseLessons(courseId);
    }

    @GetMapping("/{id}")
    public LessonResponse getLessonById(
            @PathVariable Long id) {

        return lessonService.getLessonById(id);
    }

    @PutMapping("/{id}")
    public LessonResponse updateLesson(
            @PathVariable Long id,
            @Valid @RequestBody UpdateLessonRequest request,
            @RequestHeader("X-User-Email") String userEmail,
            @RequestHeader("X-User-Role") String role) {

        return lessonService.updateLesson(
                id,
                request,
                userEmail,
                role
        );
    }

    @DeleteMapping("/{id}")
    public String deleteLesson(
            @PathVariable Long id,
            @RequestHeader("X-User-Email") String userEmail,
            @RequestHeader("X-User-Role") String role) {

        lessonService.deleteLesson(
                id,
                userEmail,
                role
        );

        return "Lesson deleted successfully";
    }
}