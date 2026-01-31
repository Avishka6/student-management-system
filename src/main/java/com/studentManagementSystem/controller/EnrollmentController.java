package com.studentManagementSystem.controller;

import com.studentManagementSystem.dto.CourseDTO;
import com.studentManagementSystem.dto.StudentDTO;
import com.studentManagementSystem.service.CourseService;
import com.studentManagementSystem.service.EnrollmentService;
import com.studentManagementSystem.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/enrollment")
public class EnrollmentController {

    private static final Logger log = LoggerFactory.getLogger(EnrollmentController.class);

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @GetMapping("/enroll")
    public String showEnrollmentPage(Model model) {
        log.info("Get /enrollment/enroll - showing enrollment page");
        
        // Get all active students and courses
        List<StudentDTO> students = studentService.getAllActiveStudents();
        List<CourseDTO> courses = courseService.getAllActiveCourses();
        
        model.addAttribute("students", students);
        model.addAttribute("courses", courses);
        
        return "enroll-course";
    }

    @PostMapping("/enroll")
    public String enrollStudent(@RequestParam("studentId") Long studentId,
                                @RequestParam("courseIds") List<Long> courseIds,
                                RedirectAttributes redirectAttributes) {
        log.info("Post /enrollment/enroll - enrolling student {} to courses {}", studentId, courseIds);
        
        try {
            enrollmentService.enrollStudentToMultipleCourses(studentId, courseIds);
            redirectAttributes.addFlashAttribute("message", "Student enrolled successfully!");
        } catch (Exception e) {
            log.error("Error enrolling student: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error enrolling student: " + e.getMessage());
        }
        
        return "redirect:/enrollment/list";
    }

    @GetMapping("/list")
    public String listEnrolledStudents(Model model) {
        log.info("Get /enrollment/list - showing enrolled students list");
        
        List<Map<String, Object>> enrolledStudents = enrollmentService.getAllEnrolledStudentsSummary();
        model.addAttribute("enrolledStudents", enrolledStudents);
        
        return "enrolled-students";
    }

    @GetMapping("/details/{studentId}")
    public String viewEnrollmentDetails(@PathVariable Long studentId, Model model) {
        log.info("Get /enrollment/details/{} - showing enrollment details", studentId);
        
        Map<String, Object> enrollmentSummary = enrollmentService.getStudentEnrollmentSummary(studentId);
        model.addAttribute("enrollmentSummary", enrollmentSummary);
        
        return "enrollment-details";
    }
}
