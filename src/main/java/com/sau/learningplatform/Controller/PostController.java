package com.sau.learningplatform.Controller;

import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.EntityResponse.PostResponse;
import com.sau.learningplatform.EntityResponse.ProjectResponse;
import com.sau.learningplatform.Service.PostService;
import com.sau.learningplatform.Service.ProjectService;
import com.sau.learningplatform.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Controller
public class PostController {
    private PostService postService;

    //using just for mocking
    private ProjectService projectService;

    private UserService userService;

    public PostController(PostService postService, ProjectService projectService, UserService userService) {
        this.postService = postService;
        this.projectService = projectService;
        this.userService = userService;
    }

    @GetMapping("/post/{projectId}")
    public String postsByProject(Principal principal, Model model, @PathVariable int projectId){
        String number = principal.getName();
        User user = userService.findByNumber(number);
        model.addAttribute("loggedUser", user);

        List<PostResponse> posts=postService.getParentPostResponsesByProjectId(projectId);
        ProjectResponse projectResponse=projectService.getResponseById(projectId);

        model.addAttribute("posts",posts);
        model.addAttribute("project",projectResponse);

        return "posts";


    }




}
