package com.learnova.discussion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learnova.discussion.entity.DiscussionThread;

public interface DiscussionThreadRepository extends JpaRepository<DiscussionThread, Long> {

    List<DiscussionThread> findByCourseIdAndActiveTrue(Long courseId);

    List<DiscussionThread> findByUserEmailAndActiveTrue(String userEmail);
}