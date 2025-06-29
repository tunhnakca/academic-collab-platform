package com.sau.learningplatform.Controller;

import com.sau.learningplatform.Entity.Project;
import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.EntityResponse.CourseResponse;
import com.sau.learningplatform.EntityResponse.MessageResponseWithStatus;
import com.sau.learningplatform.EntityResponse.ProjectResponse;
import com.sau.learningplatform.Service.CourseService;
import com.sau.learningplatform.Service.ProjectService;
import com.sau.learningplatform.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

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

    @GetMapping("/projects")
    public String projectPage(Principal principal, Model model,@RequestParam("courseCode") String courseCode) {
        String number = principal.getName();
        User user = userService.findByNumber(number);
        model.addAttribute("loggedUser", user);

        CourseResponse course=courseService.getCourseResponseByCode(courseCode);

        if (!courseService.isUserRegisteredToCourseInCurrentSemester(user,course.getId()) && !user.getRole().equalsIgnoreCase("admin")){
            return "error";
        }

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
        if (!courseService.isUserRegisteredToCourseInCurrentSemester(user,course.getId()) && !user.getRole().equalsIgnoreCase("admin")){
            return "error";
        }
        model.addAttribute("course",course);
        List<ProjectResponse> foundProjects = projectService.searchByCourseCodeAndProjectTitle(courseCode, keyword);
        model.addAttribute("projects", foundProjects);
        // it could be redirect maybe
        return "projects";
    }

    @GetMapping("/projects/add")
    public String addProjectPage(Principal principal, Model model,@RequestParam("courseCode") String courseCode) {
        String number = principal.getName();
        User user = userService.findByNumber(number);

        CourseResponse course=courseService.getCourseResponseByCode(courseCode);

        if (!courseService.isUserRegisteredToCourseInCurrentSemester(user,course.getId()) && !user.getRole().equalsIgnoreCase("admin")){
            return "error";
        }
        model.addAttribute("loggedUser", user);
        model.addAttribute("courseCode",courseCode);
        model.addAttribute("project",new Project());

        return "add-project";
    }


    @GetMapping("/projects/filter")
    public String addProjectPage(Principal principal, Model model,@RequestParam("courseCode") String courseCode,@RequestParam("filter") String queryParam) {
        String number = principal.getName();
        User user = userService.findByNumber(number);
        model.addAttribute("loggedUser", user);
        CourseResponse course=courseService.getCourseResponseByCode(courseCode);
        if (!courseService.isUserRegisteredToCourseInCurrentSemester(user,course.getId()) && !user.getRole().equalsIgnoreCase("admin")){
            return "error";
        }
        model.addAttribute("course",course);
        List<ProjectResponse>projects=projectService.filterOrSort(courseCode,queryParam);
        model.addAttribute("projects", projects);

        return "projects";

    }

    @PostMapping("/projects/add")
    public RedirectView saveNewProject(Principal principal, @ModelAttribute Project project, @RequestParam("courseCode") String courseCode, RedirectAttributes redirectAttributes) {

        String number = principal.getName();
        User user = userService.findByNumber(number);

        if (!user.getRole().equalsIgnoreCase("admin") && !user.getRole().equalsIgnoreCase("instructor")){
            return new RedirectView("/error");
        }

        MessageResponseWithStatus messageResponseWithStatus= projectService.saveProjectToCourseWithCode(project,courseCode);
        redirectAttributes.addFlashAttribute(messageResponseWithStatus);

        return new RedirectView("/projects?courseCode=" + courseCode);

    }


}