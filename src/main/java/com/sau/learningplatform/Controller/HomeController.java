package com.sau.learningplatform.Controller;

import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.EntityResponse.CourseResponse;
import com.sau.learningplatform.Service.CourseService;
import com.sau.learningplatform.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private CourseService courseService;

    private UserService userService;

    public HomeController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

    @GetMapping("/courses")
    public String homepage(Model model) {
        // mock user
        User user = userService.findById(12);
        List<CourseResponse> courses = courseService.getCoursesByUser(user);

        model.addAttribute("courses", courses);

        return "courses";

    }

    @GetMapping("/courses/add")
    public String addCoursePage(Model model) {

        return "add-course";
    }
}
