package com.sau.learningplatform.Controller;

import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.EntityResponse.CourseResponse;
import com.sau.learningplatform.EntityResponse.ProjectResponse;
import com.sau.learningplatform.Service.CourseService;
import com.sau.learningplatform.Service.ProjectService;
import com.sau.learningplatform.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class ProjectController {
    private ProjectService projectService;
    private CourseService courseService;

    private UserService userService;

    public ProjectController(ProjectService projectService, CourseService courseService, UserService userService) {
        this.projectService = projectService;
        this.courseService = courseService;
        this.userService = userService;
    }

    // it will be changed by taking courseId and will give related projects
    // it behaves like clicked into course with id=1
    @GetMapping("/projects")
    public String projectPage(Principal principal, Model model,@RequestParam("courseCode") String courseCode) {
        String number = principal.getName();
        User user = userService.findByNumber(number);
        model.addAttribute("loggedUser", user);

        CourseResponse course=courseService.getCourseResponseByCode(courseCode);

        List<ProjectResponse> projectResponses=projectService.getProjectsByCourseId(course.getId());

        model.addAttribute("course",course);
        model.addAttribute("projects", projectResponses);

        return "projects";
    }

    // it requires courseId
    @GetMapping("/projects/search")
    public String searchProjectByTitle(Principal principal, Model model, @RequestParam("keyword") String keyword, @RequestParam("courseCode") String courseCode) {
        String number = principal.getName();
        User user = userService.findByNumber(number);
        model.addAttribute("loggedUser", user);
        CourseResponse course=courseService.getCourseResponseByCode(courseCode);
        model.addAttribute("course",course);
        List<ProjectResponse> foundProjects = projectService.searchByCourseCodeAndProjectTitle(courseCode, keyword);
        model.addAttribute("projects", foundProjects);
        // it could be redirect maybe
        return "projects";
    }

    @GetMapping("/projects/add")
    public String addProjectPage(Principal principal, Model model) {
        String number = principal.getName();
        User user = userService.findByNumber(number);
        model.addAttribute("loggedUser", user);
        return "add-project";

    }

    @GetMapping("/projects/filter")
    public String addProjectPage(Principal principal, Model model,@RequestParam("courseCode") String courseCode,@RequestParam("filter") String queryParam) {
        String number = principal.getName();
        User user = userService.findByNumber(number);
        model.addAttribute("loggedUser", user);
        CourseResponse course=courseService.getCourseResponseByCode(courseCode);
        model.addAttribute("course",course);
        List<ProjectResponse>projects=projectService.filterOrSort(queryParam);
        model.addAttribute("projects", projects);

        return "projects";

    }
}