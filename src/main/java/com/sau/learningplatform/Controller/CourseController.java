package com.sau.learningplatform.Controller;

import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.EntityResponse.CourseResponse;
import com.sau.learningplatform.Service.CourseService;
import com.sau.learningplatform.Service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
public class CourseController {

    private CourseService courseService;

    private UserService userService;

    public CourseController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

    @GetMapping("/courses")
    public String homepage(Principal principal, Model model) {
        String number = principal.getName();
        User user = userService.findByNumber(number);
        List<CourseResponse> courses;
        if (user.getRole().toLowerCase().equals("admin") || user.getRole().toLowerCase().equals("admın")) {
            courses = courseService.getAllCourseResponses();
        } else {
            courses = courseService.getActiveCourseResponsesByUser(user);
        }
        model.addAttribute("courses", courses);
        model.addAttribute("loggedUser", user);

        return "courses";
    }

    @GetMapping("/courses/add")
    public String addCoursePage(Principal principal, Model model) {
        String number = principal.getName();
        User user = userService.findByNumber(number);
        model.addAttribute("loggedUser", user);
        return "add-course";

    }

    @PostMapping("/courses/add")
    public String addCourse(Principal principal,
            @RequestParam("courseName") String courseName,
            @RequestParam("courseCode") String courseCode,
            @RequestParam("file") MultipartFile studentFile,
            Model model) throws IOException {

        courseService.createCourseWithUsers(principal.getName(), courseName, courseCode, studentFile);

        return "redirect:/courses";

    }


    @DeleteMapping("/courses/delete/{courseId}")
    public ResponseEntity<String> deleteCourse(Principal principal, @PathVariable("courseId") int courseId) {
        try {
            courseService.deleteById(courseId);
            return ResponseEntity.ok("Course deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to delete project: " + e.getMessage());
        }
    }



    @PutMapping("/courses/remove/user")
    public String removeUserFromCourse(Principal principal, Model model,@RequestParam String courseCode, @RequestParam String userNumber) {
        String number = principal.getName();
        User user = userService.findByNumber(number);
        courseService.removeUserFromCourseInActiveSemester(courseCode,userNumber);
        model.addAttribute("loggedUser", user);
        return "user-list";

    }

}