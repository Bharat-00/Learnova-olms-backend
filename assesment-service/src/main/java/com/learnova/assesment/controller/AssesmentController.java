package com.learnova.assesment.controller;

import com.learnova.assesment.dto.*;
import com.learnova.assesment.service.AssesmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AssesmentController {

    private final AssesmentService assesmentService;

    @PostMapping("/quizzes")
    public QuizResponse createQuiz(
            @Valid @RequestBody CreateQuizRequest request,
            @RequestHeader("X-User-Email") String userEmail,
            @RequestHeader("X-User-Role") String role) {

        return assesmentService.createQuiz(request, userEmail, role);
    }

    @PostMapping("/quizzes/{quizId}/questions")
    public QuizResponse addQuestion(
            @PathVariable Long quizId,
            @Valid @RequestBody AddQuestionRequest request,
            @RequestHeader("X-User-Email") String userEmail,
            @RequestHeader("X-User-Role") String role) {

        return assesmentService.addQuestion(quizId, request, userEmail, role);
    }

    @GetMapping("/quizzes/course/{courseId}")
    public List<QuizResponse> getActiveQuizzesByCourse(@PathVariable Long courseId) {

        return assesmentService.getActiveQuizzesByCourse(courseId);
    }

    @GetMapping("/quizzes/{quizId}")
    public QuizResponse getQuizById(@PathVariable Long quizId) {

        return assesmentService.getQuizById(quizId);
    }

    @PutMapping("/quizzes/{quizId}")
    public QuizResponse updateQuiz(
            @PathVariable Long quizId,
            @Valid @RequestBody UpdateQuizRequest request,
            @RequestHeader("X-User-Email") String userEmail,
            @RequestHeader("X-User-Role") String role) {

        return assesmentService.updateQuiz(quizId, request, userEmail, role);
    }

    @DeleteMapping("/quizzes/{quizId}")
    public String deleteQuiz(
            @PathVariable Long quizId,
            @RequestHeader("X-User-Email") String userEmail,
            @RequestHeader("X-User-Role") String role) {

        assesmentService.deleteQuiz(quizId, userEmail, role);

        return "Quiz deleted successfully";
    }

    @PostMapping("/quizzes/{quizId}/attempts")
    public QuizAttemptResponse submitQuiz(
            @PathVariable Long quizId,
            @Valid @RequestBody SubmitQuizRequest request,
            @RequestHeader("X-User-Email") String userEmail) {

        return assesmentService.submitQuiz(quizId, request, userEmail);
    }

    @GetMapping("/attempts/me")
    public List<QuizAttemptResponse> getMyAttempts(
            @RequestHeader("X-User-Email") String userEmail) {

        return assesmentService.getMyAttempts(userEmail);
    }

    @GetMapping("/quizzes/{quizId}/attempts")
    public List<QuizAttemptResponse> getQuizAttempts(
            @PathVariable Long quizId,
            @RequestHeader("X-User-Email") String userEmail,
            @RequestHeader("X-User-Role") String role) {

        return assesmentService.getQuizAttempts(quizId, userEmail, role);
    }
}