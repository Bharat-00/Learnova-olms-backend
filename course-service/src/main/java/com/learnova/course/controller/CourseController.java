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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learnova.course.dto.CourseResponse;
import com.learnova.course.dto.CreateCourseRequest;
import com.learnova.course.dto.PagedResponse;
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
            @RequestHeader(name = "X-User-Email", required = false) String userEmail,
            @RequestHeader(name = "X-User-Role", required = false) String role) {

        return courseService.createCourse(request, userEmail, role);
    }

    @GetMapping
    public PagedResponse<CourseResponse> getCourses(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "level", required = false) String level,
            @RequestParam(name = "language", required = false) String language,
            @RequestParam(name = "isFree", required = false) Boolean isFree,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "12") int size) {

        return courseService.getCourses(search, category, isFree, page, size);
    }

    @GetMapping("/featured")
    public List<CourseResponse> getFeaturedCourses() {
        return courseService.getFeaturedCourses();
    }

    @GetMapping("/categories")
    public List<String> getCategories() {
        return courseService.getCategories();
    }

    @GetMapping("/instructor/{instructorId}")
    public List<CourseResponse> getCoursesByInstructor(
            @PathVariable(name = "instructorId") Long instructorId) {

        return courseService.getCoursesByInstructorId(instructorId);
    }

    @GetMapping("/instructor/email/{email}")
    public List<CourseResponse> getCoursesByInstructorEmail(
            @PathVariable(name = "email") String email) {

        return courseService.getCoursesByInstructorEmail(email);
    }

    @GetMapping("/{id}")
    public CourseResponse getCourseById(
            @PathVariable(name = "id") Long id) {

        return courseService.getCourseById(id);
    }

    @PutMapping("/{id}")
    public CourseResponse updateCourse(
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody UpdateCourseRequest request,
            @RequestHeader(name = "X-User-Email", required = false) String userEmail,
            @RequestHeader(name = "X-User-Role", required = false) String role) {

        return courseService.updateCourse(id, request, userEmail, role);
    }

    @PutMapping("/{id}/publish")
    public CourseResponse publishCourse(
            @PathVariable(name = "id") Long id,
            @RequestHeader(name = "X-User-Email", required = false) String userEmail,
            @RequestHeader(name = "X-User-Role", required = false) String role) {

        return courseService.publishCourse(id, userEmail, role);
    }

    @PatchMapping("/{id}/publish")
    public CourseResponse publishCoursePatch(
            @PathVariable(name = "id") Long id,
            @RequestHeader(name = "X-User-Email", required = false) String userEmail,
            @RequestHeader(name = "X-User-Role", required = false) String role) {

        return courseService.publishCourse(id, userEmail, role);
    }

    @PutMapping("/{id}/unpublish")
    public CourseResponse unpublishCourse(
            @PathVariable(name = "id") Long id,
            @RequestHeader(name = "X-User-Email", required = false) String userEmail,
            @RequestHeader(name = "X-User-Role", required = false) String role) {

        return courseService.unpublishCourse(id, userEmail, role);
    }

    @PutMapping("/{id}/approve")
    public CourseResponse approveCourse(
            @PathVariable(name = "id") Long id) {

        return courseService.publishCourse(id, null, "ADMIN");
    }

    @PutMapping("/{id}/reject")
    public CourseResponse rejectCourse(
            @PathVariable(name = "id") Long id) {

        return courseService.unpublishCourse(id, null, "ADMIN");
    }

    @DeleteMapping("/{id}")
    public String deleteCourse(
            @PathVariable(name = "id") Long id,
            @RequestHeader(name = "X-User-Email", required = false) String userEmail,
            @RequestHeader(name = "X-User-Role", required = false) String role) {

        courseService.deleteCourse(id, userEmail, role);
        return "Course deleted successfully";
    }
}