package com.learnova.course.service;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.learnova.course.dto.CourseResponse;
import com.learnova.course.dto.CreateCourseRequest;
import com.learnova.course.dto.PagedResponse;
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
        String resolvedRole = normalizeRole(role, "INSTRUCTOR");
        String resolvedEmail = resolveEmail(userEmail, request.getInstructorEmail());

        if (!securityContextUtil.isInstructor(resolvedRole) && !securityContextUtil.isAdmin(resolvedRole)) {
            throw new ForbiddenException("Only instructors can create courses");
        }

        Course course = Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .price(request.getPrice())
                .instructorEmail(resolvedEmail)
                .published(false)
                .build();

        return mapToResponse(courseRepository.save(course));
    }

    public PagedResponse<CourseResponse> getCourses(String search, String category, Boolean isFree, int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.max(size, 1);

        List<CourseResponse> filteredCourses = courseRepository.findAll()
                .stream()
                .filter(course -> Boolean.TRUE.equals(course.getPublished()))
                .filter(course -> matchesSearch(course, search))
                .filter(course -> matchesCategory(course, category))
                .filter(course -> matchesFreeFilter(course, isFree))
                .sorted(Comparator.comparing(Course::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(this::mapToResponse)
                .toList();

        int total = filteredCourses.size();
        int fromIndex = Math.min(safePage * safeSize, total);
        int toIndex = Math.min(fromIndex + safeSize, total);
        List<CourseResponse> content = filteredCourses.subList(fromIndex, toIndex);
        int totalPages = total == 0 ? 0 : (int) Math.ceil((double) total / safeSize);

        return PagedResponse.<CourseResponse>builder()
                .content(content)
                .page(safePage)
                .size(safeSize)
                .totalElements(total)
                .totalPages(totalPages)
                .last(totalPages == 0 || safePage >= totalPages - 1)
                .build();
    }

    public List<CourseResponse> getPublishedCourses() {
        return courseRepository.findByPublishedTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<CourseResponse> getFeaturedCourses() {
        return courseRepository.findByPublishedTrue()
                .stream()
                .sorted(Comparator.comparing(Course::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(4)
                .map(this::mapToResponse)
                .toList();
    }

    public List<String> getCategories() {
        return courseRepository.findAll()
                .stream()
                .map(Course::getCategory)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(category -> !category.isBlank())
                .distinct()
                .sorted()
                .toList();
    }

    public List<CourseResponse> getCoursesByInstructorId(Long instructorId) {
        String instructorEmail = "instructor" + instructorId + "@learnova.com";
        return getCoursesByInstructorEmail(instructorEmail);
    }

    public List<CourseResponse> getCoursesByInstructorEmail(String email) {
        return courseRepository.findByInstructorEmail(email)
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

        return mapToResponse(courseRepository.save(course));
    }

    public CourseResponse publishCourse(Long id, String userEmail, String role) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        validateCourseOwnerOrAdmin(course, userEmail, role, "You cannot publish this course");
        course.setPublished(true);

        return mapToResponse(courseRepository.save(course));
    }

    public CourseResponse unpublishCourse(Long id, String userEmail, String role) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        validateCourseOwnerOrAdmin(course, userEmail, role, "You cannot unpublish this course");
        course.setPublished(false);

        return mapToResponse(courseRepository.save(course));
    }

    public void deleteCourse(Long id, String userEmail, String role) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        validateCourseOwnerOrAdmin(course, userEmail, role, "You cannot delete this course");
        courseRepository.delete(course);
    }

    private void validateCourseOwnerOrAdmin(Course course, String userEmail, String role, String message) {
        String resolvedRole = normalizeRole(role, "ADMIN");

        if (securityContextUtil.isAdmin(resolvedRole)) {
            return;
        }

        if (userEmail == null || userEmail.isBlank()) {
            throw new ForbiddenException(message);
        }

        boolean isOwner = course.getInstructorEmail() != null
                && course.getInstructorEmail().equalsIgnoreCase(userEmail);

        if (!isOwner) {
            throw new ForbiddenException(message);
        }
    }

    private boolean matchesSearch(Course course, String search) {
        if (search == null || search.isBlank()) {
            return true;
        }

        String keyword = search.toLowerCase(Locale.ROOT);
        return contains(course.getTitle(), keyword)
                || contains(course.getDescription(), keyword)
                || contains(course.getCategory(), keyword);
    }

    private boolean matchesCategory(Course course, String category) {
        if (category == null || category.isBlank()) {
            return true;
        }

        return course.getCategory() != null && course.getCategory().equalsIgnoreCase(category);
    }

    private boolean matchesFreeFilter(Course course, Boolean isFree) {
        if (isFree == null) {
            return true;
        }

        boolean free = course.getPrice() == null || course.getPrice() == 0;
        return isFree == free;
    }

    private boolean contains(String value, String keyword) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(keyword);
    }

    private String normalizeRole(String role, String defaultRole) {
        return role == null || role.isBlank() ? defaultRole : role;
    }

    private String resolveEmail(String headerEmail, String requestEmail) {
        if (headerEmail != null && !headerEmail.isBlank()) {
            return headerEmail;
        }

        if (requestEmail != null && !requestEmail.isBlank()) {
            return requestEmail;
        }

        return "instructor@learnova.com";
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
                .updatedAt(course.getUpdatedAt())
                .build();
    }
}
