package com.learnova.discussion.service;

import com.learnova.discussion.dto.*;
import com.learnova.discussion.entity.*;
import com.learnova.discussion.exception.ResourceNotFoundException;
import com.learnova.discussion.repository.*;
import com.learnova.discussion.security.SecurityContextUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscussionService {

    private final DiscussionThreadRepository threadRepository;
    private final DiscussionReplyRepository replyRepository;
    private final SecurityContextUtil securityContextUtil;

    public ThreadResponse createThread(
            ThreadRequest request,
            String userEmail) {

        String email =
                securityContextUtil.getUserEmail(userEmail);

        DiscussionThread thread = DiscussionThread.builder()
                .courseId(request.getCourseId())
                .userEmail(email)
                .title(request.getTitle())
                .content(request.getContent())
                .build();

        return mapThreadResponse(
                threadRepository.save(thread)
        );
    }

    public ReplyResponse createReply(
            ReplyRequest request,
            String userEmail) {

        String email =
                securityContextUtil.getUserEmail(userEmail);

        threadRepository.findById(request.getThreadId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Thread not found"));

        DiscussionReply reply = DiscussionReply.builder()
                .threadId(request.getThreadId())
                .userEmail(email)
                .content(request.getContent())
                .build();

        return mapReplyResponse(
                replyRepository.save(reply)
        );
    }

    public List<ThreadResponse> getThreadsByCourse(Long courseId) {

        return threadRepository.findByCourseIdAndActiveTrue(courseId)
                .stream()
                .map(this::mapThreadResponse)
                .toList();
    }

    public List<ReplyResponse> getRepliesByThread(Long threadId) {

        return replyRepository.findByThreadIdAndActiveTrue(threadId)
                .stream()
                .map(this::mapReplyResponse)
                .toList();
    }

    public void deleteThread(Long threadId) {

        DiscussionThread thread =
                threadRepository.findById(threadId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Thread not found"));

        thread.setActive(false);

        threadRepository.save(thread);
    }

    public void deleteReply(Long replyId) {

        DiscussionReply reply =
                replyRepository.findById(replyId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Reply not found"));

        reply.setActive(false);

        replyRepository.save(reply);
    }

    private ThreadResponse mapThreadResponse(DiscussionThread thread) {

        return ThreadResponse.builder()
                .id(thread.getId())
                .courseId(thread.getCourseId())
                .userEmail(thread.getUserEmail())
                .title(thread.getTitle())
                .content(thread.getContent())
                .active(thread.getActive())
                .createdAt(thread.getCreatedAt())
                .updatedAt(thread.getUpdatedAt())
                .build();
    }

    private ReplyResponse mapReplyResponse(DiscussionReply reply) {

        return ReplyResponse.builder()
                .id(reply.getId())
                .threadId(reply.getThreadId())
                .userEmail(reply.getUserEmail())
                .content(reply.getContent())
                .active(reply.getActive())
                .createdAt(reply.getCreatedAt())
                .updatedAt(reply.getUpdatedAt())
                .build();
    }
}