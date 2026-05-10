package com.learnova.progress.service;

import com.learnova.progress.client.LessonClient;
import com.learnova.progress.dto.CourseProgressResponse;
import com.learnova.progress.dto.LessonProgressResponse;
import com.learnova.progress.dto.LessonResponse;
import com.learnova.progress.entity.CourseProgress;
import com.learnova.progress.entity.LessonProgress;
import com.learnova.progress.exception.BadRequestException;
import com.learnova.progress.repository.CourseProgressRepository;
import com.learnova.progress.repository.LessonProgressRepository;
import com.learnova.progress.security.SecurityContextUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final LessonProgressRepository lessonProgressRepository;
    private final CourseProgressRepository courseProgressRepository;
    private final LessonClient lessonClient;
    private final SecurityContextUtil securityContextUtil;

    public LessonProgressResponse markLessonCompleted(
            Long courseId,
            Long lessonId,
            String userEmail) {

        String email =
                securityContextUtil.getUserEmail(userEmail);

        boolean alreadyCompleted =
                lessonProgressRepository
                        .existsByUserEmailAndLessonId(email, lessonId);

        if (alreadyCompleted) {
            throw new BadRequestException(
                    "Lesson already completed"
            );
        }

        LessonProgress progress = LessonProgress.builder()
                .userEmail(email)
                .courseId(courseId)
                .lessonId(lessonId)
                .completed(true)
                .build();

        LessonProgress savedProgress =
                lessonProgressRepository.save(progress);

        updateCourseProgress(email, courseId);

        return mapLessonProgress(savedProgress);
    }

    public CourseProgressResponse getCourseProgress(
            Long courseId,
            String userEmail) {

        String email =
                securityContextUtil.getUserEmail(userEmail);

        updateCourseProgress(email, courseId);

        CourseProgress progress =
                courseProgressRepository
                        .findByUserEmailAndCourseId(email, courseId)
                        .orElseGet(() -> createEmptyProgress(email, courseId));

        return mapCourseProgress(progress);
    }

    public List<LessonProgressResponse> getCompletedLessons(
            Long courseId,
            String userEmail) {

        String email =
                securityContextUtil.getUserEmail(userEmail);

        return lessonProgressRepository
                .findByUserEmailAndCourseId(email, courseId)
                .stream()
                .map(this::mapLessonProgress)
                .toList();
    }

    private void updateCourseProgress(
            String userEmail,
            Long courseId) {

        List<LessonResponse> lessons =
                lessonClient.getLessonsByCourseId(courseId);

        int totalLessons = lessons.size();

        long completedLessons =
                lessonProgressRepository
                        .countByUserEmailAndCourseIdAndCompletedTrue(
                                userEmail,
                                courseId
                        );

        double percentage = 0.0;

        if (totalLessons > 0) {
            percentage =
                    ((double) completedLessons / totalLessons) * 100;
        }

        boolean certificateEligible =
                percentage >= 80;

        CourseProgress progress =
                courseProgressRepository
                        .findByUserEmailAndCourseId(userEmail, courseId)
                        .orElse(
                                CourseProgress.builder()
                                        .userEmail(userEmail)
                                        .courseId(courseId)
                                        .build()
                        );

        progress.setTotalLessons(totalLessons);
        progress.setCompletedLessons((int) completedLessons);
        progress.setCompletionPercentage(percentage);
        progress.setCertificateEligible(certificateEligible);

        courseProgressRepository.save(progress);
    }

    private CourseProgress createEmptyProgress(
            String userEmail,
            Long courseId) {

        return CourseProgress.builder()
                .userEmail(userEmail)
                .courseId(courseId)
                .totalLessons(0)
                .completedLessons(0)
                .completionPercentage(0.0)
                .certificateEligible(false)
                .build();
    }

    private LessonProgressResponse mapLessonProgress(
            LessonProgress progress) {

        return LessonProgressResponse.builder()
                .id(progress.getId())
                .userEmail(progress.getUserEmail())
                .courseId(progress.getCourseId())
                .lessonId(progress.getLessonId())
                .completed(progress.getCompleted())
                .completedAt(progress.getCompletedAt())
                .build();
    }

    private CourseProgressResponse mapCourseProgress(
            CourseProgress progress) {

        return CourseProgressResponse.builder()
                .id(progress.getId())
                .userEmail(progress.getUserEmail())
                .courseId(progress.getCourseId())
                .totalLessons(progress.getTotalLessons())
                .completedLessons(progress.getCompletedLessons())
                .completionPercentage(progress.getCompletionPercentage())
                .certificateEligible(progress.getCertificateEligible())
                .updatedAt(progress.getUpdatedAt())
                .build();
    }
}