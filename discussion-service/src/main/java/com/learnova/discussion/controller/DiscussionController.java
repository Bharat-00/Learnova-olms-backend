package com.learnova.discussion.controller;

import com.learnova.discussion.dto.ReplyRequest;
import com.learnova.discussion.dto.ReplyResponse;
import com.learnova.discussion.dto.ThreadRequest;
import com.learnova.discussion.dto.ThreadResponse;
import com.learnova.discussion.service.DiscussionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/discussions")
@RequiredArgsConstructor
public class DiscussionController {

    private final DiscussionService discussionService;

    @PostMapping({"/threads", ""})
    public ThreadResponse createThread(
            @Valid @RequestBody ThreadRequest request,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {
        return discussionService.createThread(request, userEmail);
    }

    @PostMapping("/replies")
    public ReplyResponse createReply(
            @Valid @RequestBody ReplyRequest request,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {
        return discussionService.createReply(request, userEmail);
    }

    @GetMapping({"/threads/course/{courseId}", "/course/{courseId}"})
    public List<ThreadResponse> getThreadsByCourse(@PathVariable("courseId") Long courseId) {
        return discussionService.getThreadsByCourse(courseId);
    }

    @GetMapping("/replies/thread/{threadId}")
    public List<ReplyResponse> getRepliesByThread(@PathVariable("threadId") Long threadId) {
        return discussionService.getRepliesByThread(threadId);
    }

    @DeleteMapping("/threads/{threadId}")
    public String deleteThread(@PathVariable("threadId") Long threadId) {
        discussionService.deleteThread(threadId);
        return "Thread deleted successfully";
    }

    @DeleteMapping("/replies/{replyId}")
    public String deleteReply(@PathVariable("replyId") Long replyId) {
        discussionService.deleteReply(replyId);
        return "Reply deleted successfully";
    }
}
