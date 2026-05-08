package com.learnova.course.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learnova.course.dto.CourseResponse;
import com.learnova.course.dto.CreateCourseRequest;
import com.learnova.course.dto.UpdateCourseRequest;
import com.learnova.course.service.CourseService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public CourseResponse createCourse(
            @Valid @RequestBody CreateCourseRequest request,
            @RequestHeader("X-User-Email") String userEmail,
            @RequestHeader("X-User-Role") String role) {

        return courseService.createCourse(request, userEmail, role);
    }

    @GetMapping
    public List<CourseResponse> getPublishedCourses() {

        return courseService.getPublishedCourses();
    }

    @GetMapping("/{id}")
    public CourseResponse getCourseById(@PathVariable Long id) {

        return courseService.getCourseById(id);
    }

    @PutMapping("/{id}")
    public CourseResponse updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCourseRequest request,
            @RequestHeader("X-User-Email") String userEmail,
            @RequestHeader("X-User-Role") String role) {

        return courseService.updateCourse(
                id,
                request,
                userEmail,
                role
        );
    }

    @PatchMapping("/{id}/publish")
    public CourseResponse publishCourse(
            @PathVariable Long id,
            @RequestHeader("X-User-Email") String userEmail,
            @RequestHeader("X-User-Role") String role) {

        return courseService.publishCourse(
                id,
                userEmail,
                role
        );
    }

    @DeleteMapping("/{id}")
    public String deleteCourse(
            @PathVariable Long id,
            @RequestHeader("X-User-Email") String userEmail,
            @RequestHeader("X-User-Role") String role) {

        courseService.deleteCourse(
                id,
                userEmail,
                role
        );

        return "Course deleted successfully";
    }
}