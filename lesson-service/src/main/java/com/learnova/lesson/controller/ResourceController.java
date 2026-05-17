package com.learnova.lesson.controller;

import com.learnova.lesson.dto.LessonResponse;
import com.learnova.lesson.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final LessonService lessonService;

    @GetMapping("/course/{courseId}")
    public List<Map<String, Object>> getResourcesByCourse(@PathVariable("courseId") Long courseId) {
        return lessonService.getCourseLessons(courseId)
                .stream()
                .filter(lesson -> lesson.getResourceUrl() != null && !lesson.getResourceUrl().isBlank())
                .map(this::toResourceMap)
                .toList();
    }

    @GetMapping("/lesson/{lessonId}")
    public Map<String, Object> getResourceByLesson(@PathVariable("lessonId") Long lessonId) {
        LessonResponse lesson = lessonService.getLessonById(lessonId);
        return toResourceMap(lesson);
    }

    private Map<String, Object> toResourceMap(LessonResponse lesson) {
        return Map.of(
                "lessonId", lesson.getId(),
                "courseId", lesson.getCourseId(),
                "title", lesson.getTitle(),
                "resourceUrl", lesson.getResourceUrl() == null ? "" : lesson.getResourceUrl()
        );
    }
}
