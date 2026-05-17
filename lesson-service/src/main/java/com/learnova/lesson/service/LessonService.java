package com.learnova.lesson.service;

import com.learnova.lesson.client.CourseClient;
import com.learnova.lesson.dto.CourseResponse;
import com.learnova.lesson.dto.CreateLessonRequest;
import com.learnova.lesson.dto.LessonResponse;
import com.learnova.lesson.dto.UpdateLessonRequest;
import com.learnova.lesson.entity.Lesson;
import com.learnova.lesson.exception.ForbiddenException;
import com.learnova.lesson.exception.ResourceNotFoundException;
import com.learnova.lesson.repository.LessonRepository;
import com.learnova.lesson.security.SecurityContextUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseClient courseClient;
    private final SecurityContextUtil securityContextUtil;

    public LessonResponse createLesson(CreateLessonRequest request, String userEmail, String role) {
        String email = securityContextUtil.getUserEmail(userEmail);
        String resolvedRole = securityContextUtil.resolveRole(role);

        securityContextUtil.validateInstructorOrAdmin(resolvedRole);

        CourseResponse course = courseClient.getCourseById(request.getCourseId());

        String instructorEmail = course.getInstructorEmail();
        boolean isOwner = instructorEmail == null || instructorEmail.isBlank() || instructorEmail.equalsIgnoreCase(email);
        boolean isAdmin = resolvedRole.equalsIgnoreCase("ADMIN");

        if (!isOwner && !isAdmin) {
            throw new ForbiddenException("You cannot add lessons to this course");
        }

        Lesson lesson = Lesson.builder()
                .courseId(request.getCourseId())
                .title(request.getTitle())
                .content(request.getContent())
                .videoUrl(request.getVideoUrl())
                .resourceUrl(request.getResourceUrl())
                .lessonOrder(request.getLessonOrder())
                .instructorEmail(email)
                .build();

        return mapToResponse(lessonRepository.save(lesson));
    }

    public List<LessonResponse> getAllLessons() {
        return lessonRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<LessonResponse> getCourseLessons(Long courseId) {
        return lessonRepository.findByCourseIdOrderByLessonOrderAsc(courseId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public LessonResponse getLessonById(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        return mapToResponse(lesson);
    }

    public LessonResponse updateLesson(Long id, UpdateLessonRequest request, String userEmail, String role) {
        String email = securityContextUtil.getUserEmail(userEmail);
        String resolvedRole = securityContextUtil.resolveRole(role);

        securityContextUtil.validateInstructorOrAdmin(resolvedRole);

        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        boolean isOwner = lesson.getInstructorEmail() == null
                || lesson.getInstructorEmail().isBlank()
                || lesson.getInstructorEmail().equalsIgnoreCase(email);
        boolean isAdmin = resolvedRole.equalsIgnoreCase("ADMIN");

        if (!isOwner && !isAdmin) {
            throw new ForbiddenException("You cannot update this lesson");
        }

        lesson.setTitle(request.getTitle());
        lesson.setContent(request.getContent());
        lesson.setVideoUrl(request.getVideoUrl());
        lesson.setResourceUrl(request.getResourceUrl());
        lesson.setLessonOrder(request.getLessonOrder());

        return mapToResponse(lessonRepository.save(lesson));
    }

    public void deleteLesson(Long id, String userEmail, String role) {
        String email = securityContextUtil.getUserEmail(userEmail);
        String resolvedRole = securityContextUtil.resolveRole(role);

        securityContextUtil.validateInstructorOrAdmin(resolvedRole);

        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        boolean isOwner = lesson.getInstructorEmail() == null
                || lesson.getInstructorEmail().isBlank()
                || lesson.getInstructorEmail().equalsIgnoreCase(email);
        boolean isAdmin = resolvedRole.equalsIgnoreCase("ADMIN");

        if (!isOwner && !isAdmin) {
            throw new ForbiddenException("You cannot delete this lesson");
        }

        lessonRepository.delete(lesson);
    }

    private LessonResponse mapToResponse(Lesson lesson) {
        return LessonResponse.builder()
                .id(lesson.getId())
                .courseId(lesson.getCourseId())
                .title(lesson.getTitle())
                .content(lesson.getContent())
                .videoUrl(lesson.getVideoUrl())
                .resourceUrl(lesson.getResourceUrl())
                .lessonOrder(lesson.getLessonOrder())
                .instructorEmail(lesson.getInstructorEmail())
                .createdAt(lesson.getCreatedAt())
                .build();
    }
}
