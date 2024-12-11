package com.sau.learningplatform.Controller;

import com.sau.learningplatform.Entity.Post;
import com.sau.learningplatform.EntityResponse.PostResponse;
import com.sau.learningplatform.Service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
public class PostController {
    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public String postsByProject(Model model){
        //mock project
       int projectId=2;
       List<PostResponse> posts= postService.getPostResponsesByProjectId(projectId);
       model.addAttribute("posts",posts);

       return "home/posts";
       //return "home/posts";

    }




}
