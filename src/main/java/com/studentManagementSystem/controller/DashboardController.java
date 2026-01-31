package com.studentManagementSystem.controller;


import com.studentManagementSystem.dto.paginated.PaginatedResponseEnrollmentDTO;
import com.studentManagementSystem.service.CourseService;
import com.studentManagementSystem.service.EnrollmentService;
import com.studentManagementSystem.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;

@Controller
public class DashboardController {

    private static final Logger log = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "size", defaultValue = "2") int size,
                           Model model) {
        log.info("Loading dashboard with real data - page: {}, size: {}", page, size);
        
        
        long totalStudents = studentService.countAllStudents();
        long totalCourses = courseService.countAllCourses();
        long totalEnrollments = enrollmentService.getTotalEnrollments();
        
        
        Map<String, Object> mostPopularCourse = enrollmentService.getMostPopularCourse();
        
        
        PaginatedResponseEnrollmentDTO paginatedEnrollments = enrollmentService.getRecentEnrollmentsWithPagination(page, size);
        
      
        model.addAttribute("totalStudents", totalStudents);
        model.addAttribute("totalCourses", totalCourses);
        model.addAttribute("totalEnrollments", totalEnrollments);
        model.addAttribute("mostPopularCourse", mostPopularCourse);
        model.addAttribute("paginatedResponseEnrollmentDTO", paginatedEnrollments);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        
        return "dashboard";
    }
}
