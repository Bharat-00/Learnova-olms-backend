package com.learnova.course.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learnova.course.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByInstructorEmail(String instructorEmail);

    List<Course> findByPublishedTrue();
}
