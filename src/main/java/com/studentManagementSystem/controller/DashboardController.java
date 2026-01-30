package com.studentManagementSystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }



//    @GetMapping("/students.html")
//    public String students() {
//        return "students";
//    }
//
//    @GetMapping("/enroll-course.html")
//    public String enrollCourse() {
//        return "enroll-course";
//    }
}
