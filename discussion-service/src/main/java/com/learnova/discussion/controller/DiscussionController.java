package com.learnova.discussion.controller;

import com.learnova.discussion.dto.*;
import com.learnova.discussion.service.DiscussionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/discussions")
@RequiredArgsConstructor
public class DiscussionController {

    private final DiscussionService discussionService;

    @PostMapping("/threads")
    public ThreadResponse createThread(
            @Valid @RequestBody ThreadRequest request,
            @RequestHeader("X-User-Email") String userEmail) {

        return discussionService.createThread(request, userEmail);
    }

    @PostMapping("/replies")
    public ReplyResponse createReply(
            @Valid @RequestBody ReplyRequest request,
            @RequestHeader("X-User-Email") String userEmail) {

        return discussionService.createReply(request, userEmail);
    }

    @GetMapping("/threads/course/{courseId}")
    public List<ThreadResponse> getThreadsByCourse(
            @PathVariable Long courseId) {

        return discussionService.getThreadsByCourse(courseId);
    }

    @GetMapping("/replies/thread/{threadId}")
    public List<ReplyResponse> getRepliesByThread(
            @PathVariable Long threadId) {

        return discussionService.getRepliesByThread(threadId);
    }

    @DeleteMapping("/threads/{threadId}")
    public String deleteThread(
            @PathVariable Long threadId) {

        discussionService.deleteThread(threadId);

        return "Thread deleted successfully";
    }

    @DeleteMapping("/replies/{replyId}")
    public String deleteReply(
            @PathVariable Long replyId) {

        discussionService.deleteReply(replyId);

        return "Reply deleted successfully";
    }
}