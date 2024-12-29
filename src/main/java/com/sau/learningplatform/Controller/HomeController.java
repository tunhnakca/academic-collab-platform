package com.sau.learningplatform.Controller;

import com.sau.learningplatform.Entity.Course;
import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.EntityResponse.CourseResponse;
import com.sau.learningplatform.EntityResponse.MessageResponse;
import com.sau.learningplatform.Service.CourseService;
import com.sau.learningplatform.Service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.security.Principal;
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
    public String homepage(Principal principal, Model model) {
        String number = principal.getName();
        User user = userService.findByNumber(number);
        List<CourseResponse> courses;
        if (user.getRole().toLowerCase().equals("admin") || user.getRole().toLowerCase().equals("admın")) {
            courses = courseService.getAllCourseResponses();
        } else {
            courses = courseService.getCoursesByUser(user);
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

        courseService.addCourseWithStudentsByExcel(principal.getName(), courseName, courseCode, studentFile);

        return "redirect:/courses";

    }

    @GetMapping("/profile")
    public String profilePage(Principal principal, Model model,
            @ModelAttribute("messageResponse") MessageResponse messageResponse) {
        String number = principal.getName();
        User user = userService.findByNumber(number);
        model.addAttribute("loggedUser", user);
        model.addAttribute(messageResponse);
        return "profile";
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

    @PostMapping("/password/change")
    public RedirectView changePassword(Principal principal,
            Model model,
            RedirectAttributes attributes,
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword) {

        String number = principal.getName();
        User user = userService.findByNumber(number);

        MessageResponse messageResponse = userService.updatePassword(user, currentPassword, newPassword);

        attributes.addFlashAttribute("messageResponse", messageResponse);

        return new RedirectView("/profile");

    }


}