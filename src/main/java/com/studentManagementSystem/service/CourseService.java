package com.studentManagementSystem.service;

import com.studentManagementSystem.dto.CourseDTO;
import com.studentManagementSystem.dto.paginated.PaginatedResponseCourseDTO;
import jakarta.validation.constraints.Max;

import java.util.List;

public interface CourseService {
    CourseDTO createCourse(CourseDTO courseDTO);
    boolean existsByCourseCode(String courseCode);
    PaginatedResponseCourseDTO getCoursesByActiveWithPagination(boolean active, int page, @Max(50) int size);
    CourseDTO getCourseById(Long id);
    CourseDTO updateCourse(Long id, CourseDTO courseDTO);
    List<CourseDTO> getAllActiveCourses();
    long countActiveCourses();
    long countAllCourses();
    void toggleActiveStatus(Long id);
}
