package com.studentManagementSystem.repo;

import com.studentManagementSystem.entity.Enrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepo extends JpaRepository<Enrollment, Long> {
    
    List<Enrollment> findByStudentId(Long studentId);
    
    List<Enrollment> findByCourseIdAndActive(Long courseId, boolean active);
    
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
    
    long countByCourseId(Long courseId);
    
    Page<Enrollment> findAllByOrderByEnrolledDateDesc(Pageable pageable);
    
    @Query("SELECT e.course.id as courseId, e.course.courseName as courseName, e.course.courseCode as courseCode, COUNT(e) as enrollmentCount " +
           "FROM Enrollment e WHERE e.active = true GROUP BY e.course.id, e.course.courseName, e.course.courseCode " +
           "ORDER BY COUNT(e) DESC")
    List<Object[]> findMostPopularCourse(Pageable pageable);
}
