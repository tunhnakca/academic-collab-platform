package com.sau.learningplatform.Controller;

import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.EntityResponse.SemesterResponse;
import com.sau.learningplatform.Service.SemesterService;
import com.sau.learningplatform.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class SemesterController {
    final private UserService userService;

    final private SemesterService semesterService;

    public SemesterController(UserService userService, SemesterService semesterService) {
        this.userService = userService;
        this.semesterService = semesterService;
    }

    @GetMapping("/semester/add")
    public String showAddSemesterForm(Principal principal, Model model) {
        String number = principal.getName();
        User user = userService.findByNumber(number);
        model.addAttribute("loggedUser", user);
        model.addAttribute("semester", semesterService.getActiveSemesterResponseIfNotEmptyResponse());
        SemesterResponse emptySemester = new SemesterResponse();
        emptySemester.setIsActive(false);
        model.addAttribute("emptySemester", new SemesterResponse());
        return "add-semester";
    }

    @PostMapping("/semester/add")
    public void updateOrSaveSemester(Principal principal, Model model, @ModelAttribute("semester")SemesterResponse semesterResponse) {
        String number = principal.getName();
        User user = userService.findByNumber(number);
        model.addAttribute("loggedUser", user);

        semesterService.saveOrUpdateResponse(semesterResponse);

    }
}
