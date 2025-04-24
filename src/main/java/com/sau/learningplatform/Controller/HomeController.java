package com.sau.learningplatform.Controller;

import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.EntityResponse.MessageResponse;
import com.sau.learningplatform.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;

@Controller
public class HomeController {

    private final UserService userService;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }

    //Bunu kullan artık ************************************************************************
    @GetMapping("/get/user")
    User getUserInfo(Principal principal,Model model){
        String number = principal.getName();
        User user = userService.findByNumber(number);
        return user;
    }

    @GetMapping("/login")
    String loginPage() {

        return "login";

    }
    
    @GetMapping("/profile")
    public String showProfilePage(Principal principal, Model model,
                                  @ModelAttribute("messageResponse") MessageResponse messageResponse) {
        String number = principal.getName();
        User user = userService.findByNumber(number);
        model.addAttribute("loggedUser", user);
        model.addAttribute(messageResponse);
        return "profile";
    }

    @GetMapping("/add-semester")
    public String showAddSemester(Principal principal, Model model,
                                  @ModelAttribute("messageResponse") MessageResponse messageResponse) {
        String number = principal.getName();
        User user = userService.findByNumber(number);
        model.addAttribute("loggedUser", user);
        model.addAttribute(messageResponse);
        return "add-semester";
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
