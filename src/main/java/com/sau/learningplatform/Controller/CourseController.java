package com.sau.learningplatform.Controller;

import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.EntityResponse.CourseResponse;
import com.sau.learningplatform.Service.CourseService;
import com.sau.learningplatform.Service.UserService;
import com.sau.learningplatform.EntityResponse.MessageResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@Slf4j
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


    @PostMapping("/courses/delete/{courseId}")
    public RedirectView deleteCourse(@PathVariable("courseId") int courseId, Model model, RedirectAttributes attributes) {
        MessageResponse messageResponse=new MessageResponse();
        try {
            courseService.deleteById(courseId);
            messageResponse.setMessage("Course has been deleted successfully!");
            messageResponse.setStatus(HttpStatus.OK);
        } catch (Exception e) {
            messageResponse.setMessage("Failed to delete course!");
            messageResponse.setStatus(HttpStatus.BAD_REQUEST);
        }
        model.addAttribute("messageResponse",messageResponse);
        log.info(messageResponse.getMessage());

        attributes.addFlashAttribute("messageResponse", messageResponse);

        return new RedirectView("/courses");

    }



    @PutMapping("/courses/remove/user")
    public MessageResponse removeUserFromCourse(@RequestParam String courseCode, @RequestParam String userNumber) {

        return courseService.removeUserFromCourseInActiveSemester(courseCode,userNumber);
    }

}