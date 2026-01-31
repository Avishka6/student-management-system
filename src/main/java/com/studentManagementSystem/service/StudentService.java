package com.studentManagementSystem.service;

import com.studentManagementSystem.dto.StudentDTO;
import com.studentManagementSystem.dto.paginated.PaginatedResponseStudentDTO;
import jakarta.validation.constraints.Max;

import java.util.List;

public interface StudentService {
    StudentDTO createStudent(StudentDTO studentDTO);
    boolean existsByEmail(String email);
    PaginatedResponseStudentDTO getStudentsByActiveWithPagination(boolean active, int page, @Max(50) int size);
    StudentDTO getStudentById(Long id);
    StudentDTO updateStudent(Long id, StudentDTO studentDTO);
    List<StudentDTO> getAllActiveStudents();
    long countActiveStudents();
    long countAllStudents();
    void toggleActiveStatus(Long id);
}
