package com.studentManagementSystem.controller;

import com.studentManagementSystem.dto.CourseDTO;
import com.studentManagementSystem.dto.paginated.PaginatedResponseCourseDTO;
import com.studentManagementSystem.service.CourseService;
import com.studentManagementSystem.util.StandardResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/course")
public class CourseController {
    private static final Logger log = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;

    @GetMapping("/new")
    public String showCreateCourse(Model model) {
        log.info("Get /course/new - showing create course page.");
        model.addAttribute("courseDTO", new CourseDTO());
        return "add-course";
    }

    @GetMapping("/list")
    public String listCourses(@RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "2") int size,
                              @RequestParam(value = "active", defaultValue = "true") boolean active,
                              Model model) {
        log.info("Get /course/list - showing courses list page with page={}, size={}, active={}", page, size, active);

        PaginatedResponseCourseDTO paginatedResponseCourseDTO = courseService.getCoursesByActiveWithPagination(active, page, size);
        model.addAttribute("paginatedResponseCourseDTO", paginatedResponseCourseDTO);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("active", active);

        return "courses";
    }

    @PostMapping
    public String createCourse(@Valid @ModelAttribute("courseDTO") CourseDTO courseDTO,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        log.info("Post /course -  create course request received.");

        if (bindingResult.hasErrors()) {
            log.error("Post /course - page return due to validation error.");
            return "add-course";
        }

        if(courseService.existsByCourseCode(courseDTO.getCourseCode())){
            log.error("Post /course - pCode must be unique.");
            bindingResult.rejectValue("courseCode", "Code must be unique");
            return "add-course";
        }

        courseService.createCourse(courseDTO);
        redirectAttributes.addFlashAttribute("message", "Course created successfully!");

        log.info("Post /course - create course request received.");

        return "redirect:/course/list";
    }

    @GetMapping(
            path = "/get-courses",
            params = {"page", "size"} //pagination
    )
    public ResponseEntity<StandardResponse> getCourses(
            @RequestParam(value = "active") boolean active,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") @Max(50) int size) {

        PaginatedResponseCourseDTO paginatedResponseCourseDTO = courseService.getCoursesByActiveWithPagination(active, page, size);
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200, "Done", paginatedResponseCourseDTO), HttpStatus.OK);
    }
}
