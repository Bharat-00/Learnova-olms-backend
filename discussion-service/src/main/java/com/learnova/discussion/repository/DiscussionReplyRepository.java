package com.learnova.discussion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learnova.discussion.entity.DiscussionReply;

public interface DiscussionReplyRepository extends JpaRepository<DiscussionReply, Long> {

    List<DiscussionReply> findByThreadIdAndActiveTrue(Long threadId);
}