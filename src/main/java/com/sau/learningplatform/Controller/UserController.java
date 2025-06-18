package com.sau.learningplatform.Controller;

import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.EntityResponse.MessageResponseWithStatus;
import com.sau.learningplatform.Service.CourseService;
import com.sau.learningplatform.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;

@Controller
@Slf4j
public class UserController {
    private UserService userService;

    private CourseService courseService;

    public UserController(UserService userService, CourseService courseService) {
        this.userService = userService;
        this.courseService = courseService;
    }

    @GetMapping("/student/list")
    public String showStudentList(Model model, Principal principal, @RequestParam("courseCode") String courseCode,@RequestParam(defaultValue = "0") int pageNo,
                                  @RequestParam(defaultValue = "10") int pageSize){
        String number = principal.getName();
        User user = userService.findByNumber(number);

        model.addAttribute("loggedUser", user);
        model.addAttribute("addStudent", new User());
        //returns only students
        model.addAttribute("usersPage",userService.getPaginatedUsersByCourseCodeAndRole(courseCode,"student",pageNo,pageSize));
        //System.out.println(userService.getPaginatedUsersByCourseCodeAndRole(courseCode,"student",pageNo,pageSize));
        model.addAttribute("course",courseService.getCourseResponseByCode(courseCode));

        return "user-list";
    }



    @GetMapping("/users")
    public String showUsersPage(Principal principal, Model model) {
        String number = principal.getName();
        User user = userService.findByNumber(number);
        model.addAttribute("loggedUser", user);
        return "user-list";

    }

    @GetMapping("/users/search")
    public String searchUserByNameAndSurname(Model model, Principal principal,@RequestParam("keyword")String keyword, @RequestParam("courseCode") String courseCode,@RequestParam(defaultValue = "0") int pageNo,
                                             @RequestParam(defaultValue = "10") int pageSize){
        String number = principal.getName();
        User user = userService.findByNumber(number);

        model.addAttribute("loggedUser", user);
        model.addAttribute("addStudent", new User());
        //returns only students
        model.addAttribute("usersPage",userService.searchUsersPaged(courseCode,"student",keyword,pageNo,pageSize));
        //System.out.println(userService.searchUsersPaged(courseCode,"student",keyword,pageNo,pageSize));
        model.addAttribute("course",courseService.getCourseResponseByCode(courseCode));

        return "user-list";
    }


    @GetMapping("/forgot/password")
    public MessageResponseWithStatus sentEmail(@RequestParam String number) {

        Optional<User> user = userService.findByNumberOptional(number);

        if(user.isEmpty()){
            return new MessageResponseWithStatus("No user found with the given number. Please check and try again.",false);
        }
        if(userService.isThereActiveToken(user.get())){
            return new MessageResponseWithStatus("We have already sent an active link, please check your email address.",true);
        }

        return userService.sendResetPasswordEmail(user.get());

    }
/*
    @GetMapping("/reset-password")
    public String showResetPasswordForm(Principal principal,@RequestParam String token) {
        String number = principal.getName();
        User user = userService.findByNumber(number);

    }
*/
    @PostMapping("/reset-password")
    public String resetPasswordWithToken(@RequestParam String token,String newPassword) {

        Optional<User>user=userService.getUserByValidToken(token);

        //buraya bir sayfa lazım (token geçersiz veya süresi geçmiş)
        if(user.isEmpty()){
            return "unauthorized";
        }

        userService.resetPassword(user.get(),newPassword);

        return "/login";
    }



}
