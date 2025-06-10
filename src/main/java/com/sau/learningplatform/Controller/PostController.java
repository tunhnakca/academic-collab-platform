package com.sau.learningplatform.Controller;

import com.sau.learningplatform.Entity.Post;
import com.sau.learningplatform.Entity.Project;
import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.EntityResponse.MessageResponseWithStatus;
import com.sau.learningplatform.EntityResponse.PostRequest;
import com.sau.learningplatform.EntityResponse.PostResponse;
import com.sau.learningplatform.EntityResponse.ProjectResponse;
import com.sau.learningplatform.Service.PostService;
import com.sau.learningplatform.Service.ProjectService;
import com.sau.learningplatform.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

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
    public String getPostsByProject(Principal principal, Model model, @PathVariable int projectId){
        String number = principal.getName();
        User user = userService.findByNumber(number);
        model.addAttribute("loggedUser", user);

        List<PostResponse> posts=postService.getParentPostResponsesByProjectId(projectId);
        ProjectResponse projectResponse=projectService.getResponseById(projectId);

        model.addAttribute("posts",posts);
        model.addAttribute("project",projectResponse);

        return "posts";

    }

    @PostMapping("/post/save")
    public RedirectView savePost(Principal principal, @ModelAttribute PostRequest request, RedirectAttributes redirectAttributes){

        String number = principal.getName();
        User user = userService.findByNumber(number);
        Project project = projectService.findById(request.getProjectId());

        MessageResponseWithStatus messageResponseWithStatus=postService.savePostRequest(user,project,request);
        redirectAttributes.addFlashAttribute("messageResponseWithStatus", messageResponseWithStatus);


        return new RedirectView("/post/"+request.getProjectId());

    }
}
