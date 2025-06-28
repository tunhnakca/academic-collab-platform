package com.sau.learningplatform.Controller;

import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.EntityResponse.MessageResponseWithStatus;

import com.sau.learningplatform.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;

@Controller
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;

    }


    @GetMapping("/login")
    String showLoginPage() {

        return "login";

    }

    @GetMapping("/")
    String  showHomePage() {

        return "redirect:/courses";

    }
    
    @GetMapping("/profile")
    public String showProfilePage(Principal principal, Model model) {
        String number = principal.getName();
        User user = userService.findByNumber(number);
        model.addAttribute("loggedUser", user);
        // messageResponse sadece redirect sonrası flash olarak gelecek, burada elle model'e eklenmeyecek!
        return "profile";
}

    @PostMapping("/password/change")
    public RedirectView changePassword(Principal principal,
                                       RedirectAttributes attributes,
                                       @RequestParam("currentPassword") String currentPassword,
                                       @RequestParam("newPassword") String newPassword) {

        String number = principal.getName();
        User user = userService.findByNumber(number);

        MessageResponseWithStatus messageResponseWithStatus = userService.updatePassword(user, currentPassword, newPassword);

        attributes.addFlashAttribute("messageResponseWithStatus", messageResponseWithStatus);

        return new RedirectView("/profile");

    }

    @GetMapping("/error")
    public String showErrorPage() {
        return "error";
    }
    @GetMapping("/invalid-token")
    public String showInvalidTokenPage() {
        return "invalid-token";
    }



}
