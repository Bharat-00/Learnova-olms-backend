package com.learnova.progress.service;

import com.learnova.progress.client.LessonClient;
import com.learnova.progress.dto.CourseProgressResponse;
import com.learnova.progress.dto.LessonProgressResponse;
import com.learnova.progress.dto.LessonResponse;
import com.learnova.progress.dto.ProgressUpdateRequest;
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

    public LessonProgressResponse markLessonCompleted(Long courseId, Long lessonId, String userEmail) {
        return markLessonCompleted(null, courseId, lessonId, userEmail);
    }

    public LessonProgressResponse markLessonCompleted(Long userId, Long courseId, Long lessonId, String userEmail) {
        if (courseId == null) {
            throw new BadRequestException("Course id is required");
        }
        if (lessonId == null) {
            throw new BadRequestException("Lesson id is required");
        }

        String email = securityContextUtil.getUserEmail(userEmail, userId);

        boolean alreadyCompleted = userId != null
                ? lessonProgressRepository.existsByUserIdAndLessonId(userId, lessonId)
                : lessonProgressRepository.existsByUserEmailAndLessonId(email, lessonId);

        if (alreadyCompleted) {
            return lessonProgressRepository.findByUserEmailAndLessonId(email, lessonId)
                    .or(() -> userId == null ? java.util.Optional.empty() : lessonProgressRepository.findByUserIdAndLessonId(userId, lessonId))
                    .map(this::mapLessonProgress)
                    .orElseThrow(() -> new BadRequestException("Lesson already completed"));
        }

        LessonProgress progress = LessonProgress.builder()
                .userId(userId)
                .userEmail(email)
                .courseId(courseId)
                .lessonId(lessonId)
                .completed(true)
                .build();

        LessonProgress savedProgress = lessonProgressRepository.save(progress);
        updateCourseProgress(email, userId, courseId);
        return mapLessonProgress(savedProgress);
    }

    public LessonProgressResponse updateProgress(ProgressUpdateRequest request, String headerEmail) {
        if (request == null) {
            throw new BadRequestException("Progress request is required");
        }
        String email = request.getUserEmail() != null ? request.getUserEmail() : headerEmail;
        return markLessonCompleted(request.getUserId(), request.getCourseId(), request.getLessonId(), email);
    }

    public CourseProgressResponse getCourseProgress(Long courseId, String userEmail) {
        String email = securityContextUtil.getUserEmail(userEmail);
        updateCourseProgress(email, null, courseId);

        CourseProgress progress = courseProgressRepository
                .findByUserEmailAndCourseId(email, courseId)
                .orElseGet(() -> createEmptyProgress(email, null, courseId));

        return mapCourseProgress(progress);
    }

    public CourseProgressResponse getCourseProgressByUserId(Long userId, Long courseId) {
        String email = securityContextUtil.getUserEmail(null, userId);
        updateCourseProgress(email, userId, courseId);

        CourseProgress progress = courseProgressRepository
                .findByUserIdAndCourseId(userId, courseId)
                .or(() -> courseProgressRepository.findByUserEmailAndCourseId(email, courseId))
                .orElseGet(() -> createEmptyProgress(email, userId, courseId));

        return mapCourseProgress(progress);
    }

    public List<CourseProgressResponse> getMyProgress(String userEmail) {
        String email = securityContextUtil.getUserEmail(userEmail);
        return courseProgressRepository.findByUserEmail(email).stream()
                .map(this::mapCourseProgress)
                .toList();
    }

    public List<LessonProgressResponse> getCompletedLessons(Long courseId, String userEmail) {
        String email = securityContextUtil.getUserEmail(userEmail);
        return lessonProgressRepository
                .findByUserEmailAndCourseId(email, courseId)
                .stream()
                .map(this::mapLessonProgress)
                .toList();
    }

    private void updateCourseProgress(String userEmail, Long userId, Long courseId) {
        if (courseId == null) {
            throw new BadRequestException("Course id is required");
        }

        List<LessonResponse> lessons;
        try {
            lessons = lessonClient.getLessonsByCourseId(courseId);
        } catch (Exception ex) {
            lessons = List.of();
        }

        int totalLessons = lessons.size();

        long completedLessons = userId != null
                ? lessonProgressRepository.countByUserIdAndCourseIdAndCompletedTrue(userId, courseId)
                : lessonProgressRepository.countByUserEmailAndCourseIdAndCompletedTrue(userEmail, courseId);

        double percentage = totalLessons > 0 ? ((double) completedLessons / totalLessons) * 100 : 0.0;
        boolean certificateEligible = percentage >= 80;

        CourseProgress progress = userId != null
                ? courseProgressRepository.findByUserIdAndCourseId(userId, courseId)
                    .orElseGet(() -> courseProgressRepository.findByUserEmailAndCourseId(userEmail, courseId)
                        .orElse(CourseProgress.builder().userId(userId).userEmail(userEmail).courseId(courseId).build()))
                : courseProgressRepository.findByUserEmailAndCourseId(userEmail, courseId)
                    .orElse(CourseProgress.builder().userEmail(userEmail).courseId(courseId).build());

        progress.setUserId(userId != null ? userId : progress.getUserId());
        progress.setUserEmail(userEmail);
        progress.setCourseId(courseId);
        progress.setTotalLessons(totalLessons);
        progress.setCompletedLessons((int) completedLessons);
        progress.setCompletionPercentage(percentage);
        progress.setCertificateEligible(certificateEligible);

        courseProgressRepository.save(progress);
    }

    private CourseProgress createEmptyProgress(String userEmail, Long userId, Long courseId) {
        return CourseProgress.builder()
                .userId(userId)
                .userEmail(userEmail)
                .courseId(courseId)
                .totalLessons(0)
                .completedLessons(0)
                .completionPercentage(0.0)
                .certificateEligible(false)
                .build();
    }

    private LessonProgressResponse mapLessonProgress(LessonProgress progress) {
        return LessonProgressResponse.builder()
                .id(progress.getId())
                .userId(progress.getUserId())
                .userEmail(progress.getUserEmail())
                .courseId(progress.getCourseId())
                .lessonId(progress.getLessonId())
                .completed(progress.getCompleted())
                .completedAt(progress.getCompletedAt())
                .build();
    }

    private CourseProgressResponse mapCourseProgress(CourseProgress progress) {
        boolean completed = progress.getCompletionPercentage() != null && progress.getCompletionPercentage() >= 100.0;
        return CourseProgressResponse.builder()
                .id(progress.getId())
                .userId(progress.getUserId())
                .userEmail(progress.getUserEmail())
                .courseId(progress.getCourseId())
                .totalLessons(progress.getTotalLessons())
                .completedLessons(progress.getCompletedLessons())
                .completionPercentage(progress.getCompletionPercentage())
                .certificateEligible(progress.getCertificateEligible())
                .completed(completed)
                .updatedAt(progress.getUpdatedAt())
                .build();
    }
}
