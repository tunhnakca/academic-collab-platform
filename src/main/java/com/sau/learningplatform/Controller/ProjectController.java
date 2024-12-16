package com.sau.learningplatform.Controller;

import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.EntityResponse.ProjectResponse;
import com.sau.learningplatform.Service.ProjectService;
import com.sau.learningplatform.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@Controller
public class ProjectController {
    private ProjectService projectService;

    private UserService userService;

    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    // it will be changed by taking courseId and will give related projects
    // it behaves like clicked into course with id=1
    @GetMapping("/projects")
    public String projectPage(Principal principal, Model model) {
        String number = principal.getName();
        User user = userService.findByNumber(number);
        model.addAttribute("loggedUser", user);
        // mock projects for testing !!!
        List<ProjectResponse> projectResponses = projectService.getAllByResponse();
        model.addAttribute("courseId", 1);
        model.addAttribute("projects", projectResponses);
        return "projects-new";

    }

    // it requires courseId
    @GetMapping("/projects/search")
    public String projectPage(Principal principal, Model model, @RequestParam("keyword") String keyword,
            @RequestParam("courseId") int courseId) {
        String number = principal.getName();
        User user = userService.findByNumber(number);
        model.addAttribute("loggedUser", user);

        List<ProjectResponse> foundProjects = projectService.searchByCourseIdAndProjectTitle(courseId, keyword);
        model.addAttribute("projects", foundProjects);
        // it could be redirect maybe
        return "projects";
    }

}