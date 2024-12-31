package com.sau.learningplatform.Controller;

import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/student/list")
    public String showStudentList(Model model, Principal principal, @RequestParam("courseCode") String courseCode){
        String number = principal.getName();
        User user = userService.findByNumber(number);

        model.addAttribute("loggedUser", user);
        model.addAttribute("addStudent", new User());

        model.addAttribute("students",userService.getAllStudents());
        model.addAttribute("courseCode",courseCode);

        return "user-list";
    }

    @PostMapping("/course/add/student")
    public String addUserToCourse(@ModelAttribute("addStudent")User student, @RequestParam("courseCode") String courseCode){

        userService.addStudentToCourseAndSaveNonExistingStudent(student,courseCode);

        return "redirect:/student/list?courseCode=" + courseCode;
    }
}
