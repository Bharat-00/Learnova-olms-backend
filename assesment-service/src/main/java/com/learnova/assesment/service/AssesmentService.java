package com.learnova.assesment.service;

import com.learnova.assesment.client.CourseClient;
import com.learnova.assesment.dto.*;
import com.learnova.assesment.entity.Question;
import com.learnova.assesment.entity.Quiz;
import com.learnova.assesment.entity.QuizAttempt;
import com.learnova.assesment.exception.BadRequestException;
import com.learnova.assesment.exception.ForbiddenException;
import com.learnova.assesment.exception.ResourceNotFoundException;
import com.learnova.assesment.repository.QuestionRepository;
import com.learnova.assesment.repository.QuizAttemptRepository;
import com.learnova.assesment.repository.QuizRepository;
import com.learnova.assesment.security.SecurityContextUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssesmentService {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final CourseClient courseClient;
    private final SecurityContextUtil securityContextUtil;

    public QuizResponse createQuiz(CreateQuizRequest request, String userEmail, String role) {

        String email = securityContextUtil.getUserEmail(userEmail);
        securityContextUtil.validateInstructorOrAdmin(role);

        CourseResponse course = fetchCourseSafely(request.getCourseId(), email);

        boolean isOwner = course.getInstructorEmail() == null || course.getInstructorEmail().equalsIgnoreCase(email);
        boolean isAdmin = securityContextUtil.isAdmin(role);

        if (!isOwner && !isAdmin) {
            throw new ForbiddenException("You cannot create quiz for this course");
        }

        Quiz quiz = Quiz.builder()
                .courseId(request.getCourseId())
                .title(request.getTitle())
                .description(request.getDescription())
                .instructorEmail(email)
                .active(true)
                .build();

        return mapQuizToResponse(quizRepository.save(quiz));
    }

    public QuizResponse addQuestion(Long quizId, AddQuestionRequest request, String userEmail, String role) {

        String email = securityContextUtil.getUserEmail(userEmail);
        securityContextUtil.validateInstructorOrAdmin(role);

        Quiz quiz = getQuizEntity(quizId);
        validateQuizOwnerOrAdmin(quiz, email, role, "You cannot add questions to this quiz");

        Question question = Question.builder()
                .questionText(request.getQuestionText())
                .optionA(request.getOptionA())
                .optionB(request.getOptionB())
                .optionC(request.getOptionC())
                .optionD(request.getOptionD())
                .correctOption(request.getCorrectOption())
                .marks(request.getMarks())
                .quiz(quiz)
                .build();

        quiz.getQuestions().add(question);
        Quiz savedQuiz = quizRepository.save(quiz);

        return mapQuizToResponse(savedQuiz);
    }

    public List<QuizResponse> getActiveQuizzesByCourse(Long courseId) {

        return quizRepository.findByCourseIdAndActiveTrue(courseId)
                .stream()
                .map(this::mapQuizToResponse)
                .toList();
    }

    public List<QuizResponse> getAllQuizzes() {

        return quizRepository.findAll()
                .stream()
                .map(this::mapQuizToResponse)
                .toList();
    }

    public QuizResponse getQuizById(Long quizId) {

        return mapQuizToResponse(getQuizEntity(quizId));
    }

    public QuizResponse updateQuiz(Long quizId, UpdateQuizRequest request, String userEmail, String role) {

        String email = securityContextUtil.getUserEmail(userEmail);
        securityContextUtil.validateInstructorOrAdmin(role);

        Quiz quiz = getQuizEntity(quizId);
        validateQuizOwnerOrAdmin(quiz, email, role, "You cannot update this quiz");

        quiz.setTitle(request.getTitle());
        quiz.setDescription(request.getDescription());
        quiz.setActive(request.getActive());

        return mapQuizToResponse(quizRepository.save(quiz));
    }

    public void deleteQuiz(Long quizId, String userEmail, String role) {

        String email = securityContextUtil.getUserEmail(userEmail);
        securityContextUtil.validateInstructorOrAdmin(role);

        Quiz quiz = getQuizEntity(quizId);
        validateQuizOwnerOrAdmin(quiz, email, role, "You cannot delete this quiz");

        quizRepository.delete(quiz);
    }

    public QuizAttemptResponse submitQuiz(Long quizId, SubmitQuizRequest request, String userEmail) {

        String email = securityContextUtil.getUserEmail(userEmail);

        Quiz quiz = getQuizEntity(quizId);

        if (quiz.getActive() == null || !quiz.getActive()) {
            throw new BadRequestException("Quiz is not active");
        }

        if (quiz.getQuestions() == null || quiz.getQuestions().isEmpty()) {
            throw new BadRequestException("Quiz has no questions");
        }

        Map<Long, String> submittedAnswers = request.getAnswers()
                .stream()
                .collect(Collectors.toMap(
                        SubmitAnswerRequest::getQuestionId,
                        SubmitAnswerRequest::getSelectedOption,
                        (first, second) -> second
                ));

        int totalQuestions = quiz.getQuestions().size();
        int correctAnswers = 0;
        int totalMarks = 0;
        int scoredMarks = 0;

        for (Question question : quiz.getQuestions()) {
            totalMarks += question.getMarks();

            String selectedOption = submittedAnswers.get(question.getId());

            if (selectedOption != null
                    && selectedOption.equalsIgnoreCase(question.getCorrectOption())) {

                correctAnswers++;
                scoredMarks += question.getMarks();
            }
        }

        QuizAttempt attempt = QuizAttempt.builder()
                .quizId(quiz.getId())
                .userEmail(email)
                .totalQuestions(totalQuestions)
                .correctAnswers(correctAnswers)
                .totalMarks(totalMarks)
                .scoredMarks(scoredMarks)
                .build();

        return mapAttemptToResponse(quizAttemptRepository.save(attempt));
    }

    public List<QuizAttemptResponse> getMyAttempts(String userEmail) {

        String email = securityContextUtil.getUserEmail(userEmail);

        return quizAttemptRepository.findByUserEmail(email)
                .stream()
                .map(this::mapAttemptToResponse)
                .toList();
    }

    public List<QuizAttemptResponse> getQuizAttempts(Long quizId, String userEmail, String role) {

        String email = securityContextUtil.getUserEmail(userEmail);
        securityContextUtil.validateInstructorOrAdmin(role);

        Quiz quiz = getQuizEntity(quizId);
        validateQuizOwnerOrAdmin(quiz, email, role, "You cannot view attempts for this quiz");

        return quizAttemptRepository.findByQuizId(quizId)
                .stream()
                .map(this::mapAttemptToResponse)
                .toList();
    }

    private CourseResponse fetchCourseSafely(Long courseId, String fallbackInstructorEmail) {
        try {
            CourseResponse course = courseClient.getCourseById(courseId);
            if (course == null) {
                return CourseResponse.builder()
                        .id(courseId)
                        .instructorEmail(fallbackInstructorEmail)
                        .build();
            }
            if (course.getInstructorEmail() == null || course.getInstructorEmail().isBlank()) {
                course.setInstructorEmail(fallbackInstructorEmail);
            }
            return course;
        } catch (Exception ex) {
            return CourseResponse.builder()
                    .id(courseId)
                    .instructorEmail(fallbackInstructorEmail)
                    .build();
        }
    }

    private Quiz getQuizEntity(Long quizId) {

        return quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));
    }

    private void validateQuizOwnerOrAdmin(Quiz quiz, String userEmail, String role, String message) {

        boolean isOwner = quiz.getInstructorEmail().equalsIgnoreCase(userEmail);
        boolean isAdmin = securityContextUtil.isAdmin(role);

        if (!isOwner && !isAdmin) {
            throw new ForbiddenException(message);
        }
    }

    private QuizResponse mapQuizToResponse(Quiz quiz) {

        List<QuestionResponse> questions = quiz.getQuestions()
                .stream()
                .map(this::mapQuestionToResponse)
                .toList();

        return QuizResponse.builder()
                .id(quiz.getId())
                .courseId(quiz.getCourseId())
                .title(quiz.getTitle())
                .description(quiz.getDescription())
                .instructorEmail(quiz.getInstructorEmail())
                .active(quiz.getActive())
                .createdAt(quiz.getCreatedAt())
                .questions(questions)
                .build();
    }

    private QuestionResponse mapQuestionToResponse(Question question) {

        return QuestionResponse.builder()
                .id(question.getId())
                .questionText(question.getQuestionText())
                .optionA(question.getOptionA())
                .optionB(question.getOptionB())
                .optionC(question.getOptionC())
                .optionD(question.getOptionD())
                .marks(question.getMarks())
                .build();
    }

    private QuizAttemptResponse mapAttemptToResponse(QuizAttempt attempt) {

        return QuizAttemptResponse.builder()
                .id(attempt.getId())
                .quizId(attempt.getQuizId())
                .userEmail(attempt.getUserEmail())
                .totalQuestions(attempt.getTotalQuestions())
                .correctAnswers(attempt.getCorrectAnswers())
                .totalMarks(attempt.getTotalMarks())
                .scoredMarks(attempt.getScoredMarks())
                .attemptedAt(attempt.getAttemptedAt())
                .build();
    }
}