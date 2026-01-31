package com.studentManagementSystem.service;

import com.studentManagementSystem.dto.EnrollmentDTO;
import com.studentManagementSystem.dto.paginated.PaginatedResponseEnrollmentDTO;

import java.util.List;
import java.util.Map;

public interface EnrollmentService {
    EnrollmentDTO enrollStudent(Long studentId, Long courseId);
    List<EnrollmentDTO> getEnrollmentsByStudentId(Long studentId);
    List<EnrollmentDTO> getEnrollmentsByCourseId(Long courseId);
    EnrollmentDTO getEnrollmentById(Long id);
    void withdrawEnrollment(Long id);
    boolean isStudentEnrolled(Long studentId, Long courseId);
    long getEnrollmentCountByCourse(Long courseId);
    void enrollStudentToMultipleCourses(Long studentId, List<Long> courseIds);
    List<Map<String, Object>> getAllEnrolledStudentsSummary();
    Map<String, Object> getStudentEnrollmentSummary(Long studentId);
    List<EnrollmentDTO> getRecentEnrollments(int limit);
    PaginatedResponseEnrollmentDTO getRecentEnrollmentsWithPagination(int page, int size);
    Map<String, Object> getMostPopularCourse();
    long getTotalEnrollments();
}
