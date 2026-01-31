package com.studentManagementSystem.service.impl;

import com.studentManagementSystem.dto.EnrollmentDTO;
import com.studentManagementSystem.dto.paginated.PaginatedResponseEnrollmentDTO;
import com.studentManagementSystem.entity.Course;
import com.studentManagementSystem.entity.Enrollment;
import com.studentManagementSystem.entity.Students;
import com.studentManagementSystem.exception.NotFoundException;
import com.studentManagementSystem.repo.CourseRepo;
import com.studentManagementSystem.repo.EnrollmentRepo;
import com.studentManagementSystem.repo.StudentRepo;
import com.studentManagementSystem.service.EnrollmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class EnrollmentServiceIMPL implements EnrollmentService {

    private static final Logger log = LoggerFactory.getLogger(EnrollmentServiceIMPL.class);

    @Autowired
    private EnrollmentRepo enrollmentRepo;

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private CourseRepo courseRepo;

    @Override
    public EnrollmentDTO enrollStudent(Long studentId, Long courseId) {
        log.info("Enrolling student {} to course {}", studentId, courseId);

        if (enrollmentRepo.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new RuntimeException("Student is already enrolled in this course");
        }

        Students student = studentRepo.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found with id: " + studentId));
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found with id: " + courseId));

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrolledDate(LocalDateTime.now());
        enrollment.setActive(true);
        enrollment.setStatus("ENROLLED");

        enrollmentRepo.save(enrollment);

        return convertToDTO(enrollment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentDTO> getEnrollmentsByStudentId(Long studentId) {
        log.info("Getting enrollments for student {}", studentId);
        return enrollmentRepo.findByStudentId(studentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentDTO> getEnrollmentsByCourseId(Long courseId) {
        log.info("Getting enrollments for course {}", courseId);
        return enrollmentRepo.findByCourseIdAndActive(courseId, true).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EnrollmentDTO getEnrollmentById(Long id) {
        log.info("Getting enrollment with id {}", id);
        Enrollment enrollment = enrollmentRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Enrollment not found with id: " + id));
        return convertToDTO(enrollment);
    }

    @Override
    public void withdrawEnrollment(Long id) {
        log.info("Withdrawing enrollment {}", id);
        Enrollment enrollment = enrollmentRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Enrollment not found with id: " + id));
        enrollment.setActive(false);
        enrollment.setStatus("WITHDRAWN");
        enrollmentRepo.save(enrollment);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isStudentEnrolled(Long studentId, Long courseId) {
        return enrollmentRepo.existsByStudentIdAndCourseId(studentId, courseId);
    }

    @Override
    @Transactional(readOnly = true)
    public long getEnrollmentCountByCourse(Long courseId) {
        return enrollmentRepo.countByCourseId(courseId);
    }

    @Override
    @Transactional
    public void enrollStudentToMultipleCourses(Long studentId, List<Long> courseIds) {
        log.info("Enrolling student {} to {} courses", studentId, courseIds.size());
        
        Students student = studentRepo.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found with id: " + studentId));
        
        for (Long courseId : courseIds) {
            if (!enrollmentRepo.existsByStudentIdAndCourseId(studentId, courseId)) {
                Course course = courseRepo.findById(courseId)
                        .orElseThrow(() -> new NotFoundException("Course not found with id: " + courseId));
                
                Enrollment enrollment = new Enrollment();
                enrollment.setStudent(student);
                enrollment.setCourse(course);
                enrollment.setEnrolledDate(LocalDateTime.now());
                enrollment.setActive(true);
                enrollment.setStatus("ENROLLED");
                
                enrollmentRepo.save(enrollment);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAllEnrolledStudentsSummary() {
        log.info("Getting all enrolled students summary");
        
        List<Students> allStudents = studentRepo.findAll();
        List<Map<String, Object>> summary = new ArrayList<>();
        
        for (Students student : allStudents) {
            List<Enrollment> enrollments = enrollmentRepo.findByStudentId(student.getId());
            if (!enrollments.isEmpty()) {
                Map<String, Object> studentSummary = new HashMap<>();
                studentSummary.put("studentId", student.getId());
                studentSummary.put("studentName", student.getFirstName() + " " + student.getLastName());
                studentSummary.put("email", student.getEmail());
                studentSummary.put("coursesCount", enrollments.size());
                
                double totalFee = enrollments.stream()
                        .mapToDouble(e -> e.getCourse().getFee().doubleValue())
                        .sum();
                studentSummary.put("totalFee", totalFee);
                
                summary.add(studentSummary);
            }
        }
        
        return summary;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getStudentEnrollmentSummary(Long studentId) {
        log.info("Getting enrollment summary for student {}", studentId);
        
        Students student = studentRepo.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found with id: " + studentId));
        
        List<Enrollment> enrollments = enrollmentRepo.findByStudentId(studentId);
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("studentId", student.getId());
        summary.put("studentName", student.getFirstName() + " " + student.getLastName());
        summary.put("email", student.getEmail());
        summary.put("coursesCount", enrollments.size());
        
        double totalFee = enrollments.stream()
                .mapToDouble(e -> e.getCourse().getFee().doubleValue())
                .sum();
        summary.put("totalFee", totalFee);
        
        List<Map<String, Object>> courses = new ArrayList<>();
        for (Enrollment enrollment : enrollments) {
            Map<String, Object> courseInfo = new HashMap<>();
            courseInfo.put("enrollmentId", enrollment.getId());
            courseInfo.put("courseName", enrollment.getCourse().getCourseName());
            courseInfo.put("courseCode", enrollment.getCourse().getCourseCode());
            courseInfo.put("description", enrollment.getCourse().getDescription());
            courseInfo.put("fee", enrollment.getCourse().getFee());
            courseInfo.put("enrolledDate", enrollment.getEnrolledDate());
            courseInfo.put("status", enrollment.getStatus());
            courses.add(courseInfo);
        }
        summary.put("courses", courses);
        
        return summary;
    }

    private EnrollmentDTO convertToDTO(Enrollment enrollment) {
        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setId(enrollment.getId());
        dto.setStudentId(enrollment.getStudent().getId());
        dto.setStudentName(enrollment.getStudent().getFirstName() + " " + enrollment.getStudent().getLastName());
        dto.setStudentEmail(enrollment.getStudent().getEmail());
        dto.setCourseId(enrollment.getCourse().getId());
        dto.setCourseName(enrollment.getCourse().getCourseName());
        dto.setCourseCode(enrollment.getCourse().getCourseCode());
        dto.setEnrolledDate(enrollment.getEnrolledDate());
        dto.setActive(enrollment.isActive());
        dto.setStatus(enrollment.getStatus());
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentDTO> getRecentEnrollments(int limit) {
        log.info("Getting recent {} enrollments", limit);
        Page<Enrollment> enrollmentPage = enrollmentRepo.findAllByOrderByEnrolledDateDesc(PageRequest.of(0, limit));
        return enrollmentPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponseEnrollmentDTO getRecentEnrollmentsWithPagination(int page, int size) {
        log.info("Getting recent enrollments with pagination - page: {}, size: {}", page, size);
        Page<Enrollment> enrollmentPage = enrollmentRepo.findAllByOrderByEnrolledDateDesc(PageRequest.of(page, size));
        
        List<EnrollmentDTO> enrollmentDTOs = enrollmentPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return new PaginatedResponseEnrollmentDTO(
                enrollmentDTOs,
                enrollmentPage.getTotalElements()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getMostPopularCourse() {
        log.info("Getting most popular course");
        List<Object[]> results = enrollmentRepo.findMostPopularCourse(PageRequest.of(0, 1));
        
        if (results.isEmpty()) {
            Map<String, Object> empty = new HashMap<>();
            empty.put("courseCode", "N/A");
            empty.put("courseName", "No enrollments yet");
            empty.put("enrollmentCount", 0L);
            return empty;
        }
        
        Object[] result = results.get(0);
        Map<String, Object> courseInfo = new HashMap<>();
        courseInfo.put("courseId", result[0]);
        courseInfo.put("courseName", result[1]);
        courseInfo.put("courseCode", result[2]);
        courseInfo.put("enrollmentCount", result[3]);
        return courseInfo;
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalEnrollments() {
        log.info("Getting total enrollments count");
        return enrollmentRepo.count();
    }
}
