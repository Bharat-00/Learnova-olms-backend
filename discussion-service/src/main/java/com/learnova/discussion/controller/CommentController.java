package com.learnova.discussion.controller;

import com.learnova.discussion.dto.ReplyRequest;
import com.learnova.discussion.dto.ReplyResponse;
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
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final DiscussionService discussionService;

    @PostMapping
    public ReplyResponse createComment(
            @Valid @RequestBody ReplyRequest request,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {
        return discussionService.createReply(request, userEmail);
    }

    @GetMapping("/thread/{threadId}")
    public List<ReplyResponse> getCommentsByThread(@PathVariable("threadId") Long threadId) {
        return discussionService.getRepliesByThread(threadId);
    }

    @DeleteMapping("/{replyId}")
    public String deleteComment(@PathVariable("replyId") Long replyId) {
        discussionService.deleteReply(replyId);
        return "Comment deleted successfully";
    }
}
