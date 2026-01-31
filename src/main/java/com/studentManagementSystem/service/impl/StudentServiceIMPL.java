package com.studentManagementSystem.service.impl;

import com.studentManagementSystem.dto.StudentDTO;
import com.studentManagementSystem.dto.paginated.PaginatedResponseStudentDTO;
import com.studentManagementSystem.entity.Students;
import com.studentManagementSystem.exception.NotFoundException;
import com.studentManagementSystem.repo.StudentRepo;
import com.studentManagementSystem.service.StudentService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentServiceIMPL implements StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentServiceIMPL.class);

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public StudentDTO createStudent(StudentDTO studentDTO) {
        log.info("creating student with email: {}", studentDTO.getEmail());
        Students student = modelMapper.map(studentDTO, Students.class);
        student.setCreatedAt(LocalDateTime.now());
        studentRepo.save(student);
        return modelMapper.map(student, StudentDTO.class);
    }

    @Override
    public boolean existsByEmail(String email) {
        log.info("checking if email exists: {}", email);
        return studentRepo.existsByEmailIgnoreCase(email);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponseStudentDTO getStudentsByActiveWithPagination(boolean active, int page, int size) {
        Page<Students> students = studentRepo.findAllByActive(active, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
        
        PaginatedResponseStudentDTO paginatedResponseStudentDTO = new PaginatedResponseStudentDTO(
                students.map(student -> modelMapper.map(student, StudentDTO.class)).toList(),
                studentRepo.countAllByActive(active)
        );

        return paginatedResponseStudentDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDTO getStudentById(Long id) {
        Students student = studentRepo.findById(id).orElseThrow(() -> new NotFoundException("Student not found with id: " + id));
        return modelMapper.map(student, StudentDTO.class);
    }

    @Override
    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        log.info("updating student with id: {}", id);
        Students student = studentRepo.findById(id).orElseThrow(() -> new NotFoundException("Student not found with id: " + id));
        
        student.setFirstName(studentDTO.getFirstName());
        student.setLastName(studentDTO.getLastName());
        student.setEmail(studentDTO.getEmail());
        student.setPhoneNumber(studentDTO.getPhoneNumber());
        student.setAddress(studentDTO.getAddress());
        student.setActive(studentDTO.isActive());
        
        studentRepo.save(student);
        return modelMapper.map(student, StudentDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDTO> getAllActiveStudents() {
        log.info("getting all active students");
        return studentRepo.findAllByActive(true).stream()
                .map(student -> modelMapper.map(student, StudentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long countActiveStudents() {
        log.info("counting active students");
        return studentRepo.countAllByActive(true);
    }

    @Override
    @Transactional(readOnly = true)
    public long countAllStudents() {
        log.info("counting all students");
        return studentRepo.count();
    }

    @Override
    public void toggleActiveStatus(Long id) {
        log.info("toggling active status for student with id: {}", id);
        Students student = studentRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found with id: " + id));
        student.setActive(!student.isActive());
        studentRepo.save(student);
        log.info("Student id: {} active status changed to: {}", id, student.isActive());
    }
}
