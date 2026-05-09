package com.learnova.lesson.service;

import com.learnova.lesson.client.CourseClient;
import com.learnova.lesson.dto.*;
import com.learnova.lesson.entity.Lesson;
import com.learnova.lesson.exception.*;
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

    public LessonResponse createLesson(
            CreateLessonRequest request,
            String userEmail,
            String role) {

        String email =
                securityContextUtil.getUserEmail(userEmail);

        securityContextUtil.validateInstructorOrAdmin(role);

        CourseResponse course =
                courseClient.getCourseById(request.getCourseId());

        boolean isOwner =
                course.getInstructorEmail()
                        .equalsIgnoreCase(email);

        boolean isAdmin =
                role.equalsIgnoreCase("ADMIN");

        if (!isOwner && !isAdmin) {

            throw new ForbiddenException(
                    "You cannot add lessons to this course"
            );
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

        Lesson savedLesson =
                lessonRepository.save(lesson);

        return mapToResponse(savedLesson);
    }

    public List<LessonResponse> getCourseLessons(Long courseId) {

        return lessonRepository
                .findByCourseIdOrderByLessonOrderAsc(courseId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public LessonResponse getLessonById(Long id) {

        Lesson lesson =
                lessonRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Lesson not found"
                                ));

        return mapToResponse(lesson);
    }

    public LessonResponse updateLesson(
            Long id,
            UpdateLessonRequest request,
            String userEmail,
            String role) {

        String email =
                securityContextUtil.getUserEmail(userEmail);

        securityContextUtil.validateInstructorOrAdmin(role);

        Lesson lesson =
                lessonRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Lesson not found"
                                ));

        boolean isOwner =
                lesson.getInstructorEmail()
                        .equalsIgnoreCase(email);

        boolean isAdmin =
                role.equalsIgnoreCase("ADMIN");

        if (!isOwner && !isAdmin) {

            throw new ForbiddenException(
                    "You cannot update this lesson"
            );
        }

        lesson.setTitle(request.getTitle());
        lesson.setContent(request.getContent());
        lesson.setVideoUrl(request.getVideoUrl());
        lesson.setResourceUrl(request.getResourceUrl());
        lesson.setLessonOrder(request.getLessonOrder());

        Lesson updatedLesson =
                lessonRepository.save(lesson);

        return mapToResponse(updatedLesson);
    }

    public void deleteLesson(
            Long id,
            String userEmail,
            String role) {

        String email =
                securityContextUtil.getUserEmail(userEmail);

        securityContextUtil.validateInstructorOrAdmin(role);

        Lesson lesson =
                lessonRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Lesson not found"
                                ));

        boolean isOwner =
                lesson.getInstructorEmail()
                        .equalsIgnoreCase(email);

        boolean isAdmin =
                role.equalsIgnoreCase("ADMIN");

        if (!isOwner && !isAdmin) {

            throw new ForbiddenException(
                    "You cannot delete this lesson"
            );
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