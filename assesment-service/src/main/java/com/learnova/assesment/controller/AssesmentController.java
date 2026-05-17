package com.learnova.assesment.controller;

import com.learnova.assesment.dto.*;
import com.learnova.assesment.service.AssesmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AssesmentController {

    private static final String DEFAULT_EMAIL = "student@learnova.local";
    private static final String DEFAULT_ROLE = "ADMIN";

    private final AssesmentService assesmentService;

    @PostMapping({"/api/v1/quizzes", "/api/v1/assesments", "/api/v1/assessments"})
    public QuizResponse createQuiz(
            @Valid @RequestBody CreateQuizRequest request,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail,
            @RequestHeader(value = "X-User-Role", required = false) String role) {

        return assesmentService.createQuiz(request, resolveEmail(userEmail), resolveRole(role));
    }

    @PostMapping({
            "/api/v1/quizzes/{quizId}/questions",
            "/api/v1/assesments/{quizId}/questions",
            "/api/v1/assessments/{quizId}/questions",
            "/api/v1/questions/quizzes/{quizId}"
    })
    public QuizResponse addQuestion(
            @PathVariable("quizId") Long quizId,
            @Valid @RequestBody AddQuestionRequest request,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail,
            @RequestHeader(value = "X-User-Role", required = false) String role) {

        return assesmentService.addQuestion(quizId, request, resolveEmail(userEmail), resolveRole(role));
    }

    @GetMapping({
            "/api/v1/quizzes/course/{courseId}",
            "/api/v1/assesments/course/{courseId}",
            "/api/v1/assessments/course/{courseId}"
    })
    public List<QuizResponse> getActiveQuizzesByCourse(@PathVariable("courseId") Long courseId) {
        return assesmentService.getActiveQuizzesByCourse(courseId);
    }

    @GetMapping({"/api/v1/quizzes", "/api/v1/assesments", "/api/v1/assessments"})
    public List<QuizResponse> getAllQuizzes() {
        return assesmentService.getAllQuizzes();
    }

    @GetMapping({"/api/v1/quizzes/{quizId}", "/api/v1/assesments/{quizId}", "/api/v1/assessments/{quizId}"})
    public QuizResponse getQuizById(@PathVariable("quizId") Long quizId) {
        return assesmentService.getQuizById(quizId);
    }

    @PutMapping({"/api/v1/quizzes/{quizId}", "/api/v1/assesments/{quizId}", "/api/v1/assessments/{quizId}"})
    public QuizResponse updateQuiz(
            @PathVariable("quizId") Long quizId,
            @Valid @RequestBody UpdateQuizRequest request,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail,
            @RequestHeader(value = "X-User-Role", required = false) String role) {

        return assesmentService.updateQuiz(quizId, request, resolveEmail(userEmail), resolveRole(role));
    }

    @DeleteMapping({"/api/v1/quizzes/{quizId}", "/api/v1/assesments/{quizId}", "/api/v1/assessments/{quizId}"})
    public String deleteQuiz(
            @PathVariable("quizId") Long quizId,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail,
            @RequestHeader(value = "X-User-Role", required = false) String role) {

        assesmentService.deleteQuiz(quizId, resolveEmail(userEmail), resolveRole(role));
        return "Quiz deleted successfully";
    }

    @PostMapping({
            "/api/v1/quizzes/{quizId}/attempts",
            "/api/v1/assesments/{quizId}/attempts",
            "/api/v1/assessments/{quizId}/attempts",
            "/api/v1/submissions/quizzes/{quizId}",
            "/api/v1/submissions/assesments/{quizId}",
            "/api/v1/submissions/assessments/{quizId}"
    })
    public QuizAttemptResponse submitQuiz(
            @PathVariable("quizId") Long quizId,
            @Valid @RequestBody SubmitQuizRequest request,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {

        return assesmentService.submitQuiz(quizId, request, resolveEmail(userEmail));
    }

    @GetMapping({"/api/v1/attempts/me", "/api/v1/submissions/me"})
    public List<QuizAttemptResponse> getMyAttempts(
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {

        return assesmentService.getMyAttempts(resolveEmail(userEmail));
    }

    @GetMapping({
            "/api/v1/quizzes/{quizId}/attempts",
            "/api/v1/assesments/{quizId}/attempts",
            "/api/v1/assessments/{quizId}/attempts",
            "/api/v1/submissions/quizzes/{quizId}"
    })
    public List<QuizAttemptResponse> getQuizAttempts(
            @PathVariable("quizId") Long quizId,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail,
            @RequestHeader(value = "X-User-Role", required = false) String role) {

        return assesmentService.getQuizAttempts(quizId, resolveEmail(userEmail), resolveRole(role));
    }

    private String resolveEmail(String userEmail) {
        return userEmail == null || userEmail.isBlank() ? DEFAULT_EMAIL : userEmail;
    }

    private String resolveRole(String role) {
        return role == null || role.isBlank() ? DEFAULT_ROLE : role;
    }
}
