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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

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



    @PostMapping("/forgot/password")
    public RedirectView sentEmail(@RequestParam String number, RedirectAttributes redirectAttributes) {

        Optional<User> user = userService.findByNumberOptional(number);
        MessageResponseWithStatus messageResponseWithStatus;
        if(user.isEmpty()){
            messageResponseWithStatus= new MessageResponseWithStatus("No user found with the given number. Please check and try again.",false);
        }
        else {
            messageResponseWithStatus=userService.sendResetPasswordEmail(user.get());
        }
        redirectAttributes.addFlashAttribute(messageResponseWithStatus);

        return new RedirectView("/login");

    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam String token, Model model) {

        Optional<User>user=userService.getUserByValidToken(token);

        //buraya bir sayfa lazım (token geçersiz veya süresi geçmiş)
        if(user.isEmpty()){
            return "invalid-token";
        }

        model.addAttribute("token",token);

        return "reset-password";
    }

    @PostMapping("/reset-password")
    public RedirectView resetPasswordWithToken(@RequestParam String token,String newPassword,String confirmNewPassword,RedirectAttributes redirectAttributes) {

        Optional<User>user=userService.getUserByValidToken(token);


        if(user.isEmpty()){
            return new RedirectView("/invalid-token");
        }

        MessageResponseWithStatus messageResponseWithStatus=userService.resetPassword(user.get(),newPassword,confirmNewPassword);
        redirectAttributes.addFlashAttribute(messageResponseWithStatus);

        if (!messageResponseWithStatus.getIsStatusOk()){
            return new RedirectView("/reset-password?token="+token);
        }

        return new RedirectView("/login");
    }

    
}


