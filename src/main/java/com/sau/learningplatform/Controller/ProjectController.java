package com.sau.learningplatform.Controller;

import com.sau.learningplatform.Entity.Project;
import com.sau.learningplatform.Service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
public class ProjectController {
    private ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/projects")
    public String ProjectsByCourses(Model model){
        //mock course id
        int courseId=1;
        List<Project>projects=projectService.getProjectsByCourseId(courseId);
        model.addAttribute("projects",projects);
        return "home/projects";
    }
}
