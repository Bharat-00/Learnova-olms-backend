package com.learnova.lesson.controller;

import com.learnova.lesson.dto.CreateLessonRequest;
import com.learnova.lesson.dto.LessonResponse;
import com.learnova.lesson.dto.UpdateLessonRequest;
import com.learnova.lesson.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @PostMapping
    public LessonResponse createLesson(
            @Valid @RequestBody CreateLessonRequest request,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail,
            @RequestHeader(value = "X-User-Role", required = false) String role) {

        return lessonService.createLesson(request, userEmail, role);
    }

    @GetMapping
    public List<LessonResponse> getAllLessons() {
        return lessonService.getAllLessons();
    }

    @GetMapping("/course/{courseId}")
    public List<LessonResponse> getCourseLessons(@PathVariable("courseId") Long courseId) {
        return lessonService.getCourseLessons(courseId);
    }

    @GetMapping("/{id}")
    public LessonResponse getLessonById(@PathVariable("id") Long id) {
        return lessonService.getLessonById(id);
    }

    @PutMapping("/{id}")
    public LessonResponse updateLesson(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateLessonRequest request,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail,
            @RequestHeader(value = "X-User-Role", required = false) String role) {

        return lessonService.updateLesson(id, request, userEmail, role);
    }

    @DeleteMapping("/{id}")
    public String deleteLesson(
            @PathVariable("id") Long id,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail,
            @RequestHeader(value = "X-User-Role", required = false) String role) {

        lessonService.deleteLesson(id, userEmail, role);
        return "Lesson deleted successfully";
    }
}
