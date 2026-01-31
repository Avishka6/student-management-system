package com.studentManagementSystem.controller;

import com.studentManagementSystem.dto.StudentDTO;
import com.studentManagementSystem.dto.paginated.PaginatedResponseStudentDTO;
import com.studentManagementSystem.service.StudentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/students")
public class StudentController {

    private static final Logger log = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentService studentService;

    @GetMapping
    public String redirectToStudentsList() {
        log.info("Get /students - redirecting to students list");
        return "redirect:/students/list";
    }

    @GetMapping("/new")
    public String showCreateStudent(Model model) {
        log.info("Get /students/new - showing create student page.");
        model.addAttribute("studentDTO", new StudentDTO());
        return "add-student";
    }

    @GetMapping("/list")
    public String listStudents(@RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "2") int size,
                              @RequestParam(value = "active", defaultValue = "true") boolean active,
                              Model model) {
        log.info("Get /students/list - showing students list page with page={}, size={}, active={}", page, size, active);

        PaginatedResponseStudentDTO paginatedResponseStudentDTO = studentService.getStudentsByActiveWithPagination(active, page, size);
        model.addAttribute("paginatedResponseStudentDTO", paginatedResponseStudentDTO);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("active", active);

        return "students";
    }

    @PostMapping
    public String createStudent(@Valid @ModelAttribute("studentDTO") StudentDTO studentDTO,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        log.info("Post /students - create student request received.");

        if (bindingResult.hasErrors()) {
            log.error("Post /students - page return due to validation error.");
            return "add-student";
        }

        if(studentService.existsByEmail(studentDTO.getEmail())){
            log.error("Post /students - email must be unique.");
            bindingResult.rejectValue("email", "Email already exists");
            return "add-student";
        }

        studentService.createStudent(studentDTO);
        redirectAttributes.addFlashAttribute("message", "Student created successfully!");

        log.info("Post /students - create student request completed.");

        return "redirect:/students/list";
    }

    @GetMapping("/{id}")
    public String viewStudent(@PathVariable Long id, Model model) {
        log.info("Get /students/{} - showing student details", id);
        StudentDTO studentDTO = studentService.getStudentById(id);
        model.addAttribute("student", studentDTO);
        return "view-student";
    }

    @GetMapping("/{id}/edit")
    public String showEditStudent(@PathVariable Long id, Model model) {
        log.info("Get /students/{}/edit - showing edit student page", id);
        StudentDTO studentDTO = studentService.getStudentById(id);
        model.addAttribute("studentDTO", studentDTO);
        return "edit-student";
    }

    @PostMapping("/{id}/update")
    public String updateStudent(@PathVariable Long id,
                               @Valid @ModelAttribute("studentDTO") StudentDTO studentDTO,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        log.info("Post /students/{}/update - update student request received", id);

        if (bindingResult.hasErrors()) {
            log.error("Post /students/{}/update - page return due to validation error", id);
            studentDTO.setId(id);
            model.addAttribute("studentDTO", studentDTO);
            return "edit-student";
        }

        studentService.updateStudent(id, studentDTO);
        redirectAttributes.addFlashAttribute("message", "Student updated successfully!");

        log.info("Post /students/{}/update - student updated successfully", id);

        return "redirect:/students/list";
    }

    @PostMapping("/{id}/toggle-status")
    public String toggleStudentStatus(@PathVariable Long id, 
                                      @RequestParam(value = "active", defaultValue = "true") boolean active,
                                      RedirectAttributes redirectAttributes) {
        log.info("Post /students/{}/toggle-status - toggling student active status", id);
        studentService.toggleActiveStatus(id);
        redirectAttributes.addFlashAttribute("message", "Student status updated successfully!");
        return "redirect:/students/list?active=" + active;
    }
}
