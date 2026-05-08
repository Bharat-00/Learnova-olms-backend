package com.learnova.course.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.learnova.course.dto.CourseResponse;
import com.learnova.course.dto.CreateCourseRequest;
import com.learnova.course.dto.UpdateCourseRequest;
import com.learnova.course.entity.Course;
import com.learnova.course.exception.ForbiddenException;
import com.learnova.course.exception.ResourceNotFoundException;
import com.learnova.course.repository.CourseRepository;
import com.learnova.course.security.SecurityContextUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final SecurityContextUtil securityContextUtil;

    public CourseResponse createCourse(CreateCourseRequest request, String userEmail, String role) {

        if (!securityContextUtil.isInstructor(role) && !securityContextUtil.isAdmin(role)) {
            throw new ForbiddenException("Only instructors can create courses");
        }

        Course course = Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .price(request.getPrice())
                .instructorEmail(userEmail)
                .published(false)
                .build();

        Course savedCourse = courseRepository.save(course);

        return mapToResponse(savedCourse);
    }

    public List<CourseResponse> getPublishedCourses() {

        return courseRepository.findByPublishedTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public CourseResponse getCourseById(Long id) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        return mapToResponse(course);
    }

    public CourseResponse updateCourse(Long id, UpdateCourseRequest request, String userEmail, String role) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        validateCourseOwnerOrAdmin(course, userEmail, role, "You cannot update this course");

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setCategory(request.getCategory());
        course.setPrice(request.getPrice());

        Course updatedCourse = courseRepository.save(course);

        return mapToResponse(updatedCourse);
    }

    public CourseResponse publishCourse(Long id, String userEmail, String role) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        validateCourseOwnerOrAdmin(course, userEmail, role, "You cannot publish this course");

        course.setPublished(true);

        Course publishedCourse = courseRepository.save(course);

        return mapToResponse(publishedCourse);
    }

    public void deleteCourse(Long id, String userEmail, String role) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        validateCourseOwnerOrAdmin(course, userEmail, role, "You cannot delete this course");

        courseRepository.delete(course);
    }

    private void validateCourseOwnerOrAdmin(Course course, String userEmail, String role, String message) {

        boolean isOwner = course.getInstructorEmail().equalsIgnoreCase(userEmail);
        boolean isAdmin = securityContextUtil.isAdmin(role);

        if (!isOwner && !isAdmin) {
            throw new ForbiddenException(message);
        }
    }

    private CourseResponse mapToResponse(Course course) {

        return CourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .category(course.getCategory())
                .price(course.getPrice())
                .instructorEmail(course.getInstructorEmail())
                .published(course.getPublished())
                .createdAt(course.getCreatedAt())
                .build();
    }
}