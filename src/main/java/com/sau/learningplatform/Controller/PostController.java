package com.sau.learningplatform.Controller;

import com.sau.learningplatform.Entity.Project;
import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.EntityResponse.*;
import com.sau.learningplatform.Service.PostService;
import com.sau.learningplatform.Service.ProjectService;
import com.sau.learningplatform.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    public String getPostsByProject(Principal principal, Model model, @PathVariable int projectId,@RequestParam(defaultValue = "0") int pageNo,
                                    @RequestParam(defaultValue = "10") int pageSize){
        String number = principal.getName();
        User user = userService.findByNumber(number);

        model.addAttribute("loggedUser", user);

        PostPageResponse postPageResponse=postService.getParentPostsAsPostPageResponseByProjectId(projectId,pageNo,pageSize);

        ProjectResponse projectResponse=projectService.getResponseById(projectId);

        model.addAttribute("postPageResponse",postPageResponse);
        model.addAttribute("project",projectResponse);

        return "posts";
    }

    @GetMapping("/post/search/{projectId}")
    public String searchPostsByText(Principal principal, Model model, @PathVariable int projectId,@RequestParam(defaultValue = "0") int pageNo,
                                    @RequestParam(defaultValue = "10") int pageSize,@RequestParam String keyword){
        String number = principal.getName();
        User user = userService.findByNumber(number);
        model.addAttribute("loggedUser", user);

        PostPageResponse postPageResponse=postService.searchParentPostsAsPostPageResponseByProjectId(keyword,projectId,pageNo,pageSize);

        ProjectResponse projectResponse=projectService.getResponseById(projectId);

        model.addAttribute("postPageResponse",postPageResponse);
        model.addAttribute("project",projectResponse);

        return "posts";
    }


    //page no alınmalı
    @PostMapping("/post/save")
    public RedirectView savePost(Principal principal
            ,@ModelAttribute PostRequest request, RedirectAttributes redirectAttributes
            ,@RequestParam(defaultValue = "0") int pageNo){

        String number = principal.getName();
        User user = userService.findByNumber(number);
        Project project = projectService.findById(request.getProjectId());

        MessageResponseWithStatus messageResponseWithStatus=postService.savePostRequest(user,project,request);
        redirectAttributes.addFlashAttribute("messageResponseWithStatus", messageResponseWithStatus);


        return new RedirectView("/post/"+request.getProjectId()+"?pageNo="+pageNo);

    }

    //current page no, projectId ve silinecek postId alınmalı
    @DeleteMapping("/post/delete")
    public RedirectView deletePost(Principal principal
            ,@RequestParam int postId
            ,@RequestParam int projectId
            ,@RequestParam(defaultValue = "0") int pageNo
            ,RedirectAttributes redirectAttributes){

        String number = principal.getName();
        User loggedUser = userService.findByNumber(number);


        MessageResponseWithStatus messageResponseWithStatus=postService.deletePost(loggedUser,postId);

        redirectAttributes.addFlashAttribute("messageResponseWithStatus", messageResponseWithStatus);


        return new RedirectView("/post/"+projectId+"?pageNo="+pageNo);

    }








}
