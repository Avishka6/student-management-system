package com.studentManagementSystem.repo;

import com.studentManagementSystem.entity.Course;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface CourseRepo extends JpaRepository<Course, Long> {
    boolean existsByCourseCodeIgnoreCase(String courseCode);

    Page<Course> findAllByActive(boolean active, Pageable pageable);

    long countAllByActive(boolean active);
}
