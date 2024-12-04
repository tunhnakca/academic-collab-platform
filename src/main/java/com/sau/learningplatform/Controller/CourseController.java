package com.sau.learningplatform.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class CourseController {
    @GetMapping("/courses")
    public String getCourses(Model model) {
        // Add logic to fetch courses
        return "courses";
    }

    @GetMapping("/courses/add")
    public String addCourses(Model model) {
        // Add logic to fetch courses
        return "add-course";
    }
}
