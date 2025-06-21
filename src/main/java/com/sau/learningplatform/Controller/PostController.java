package com.sau.learningplatform.Controller;

import com.sau.learningplatform.Entity.Project;
import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.EntityResponse.*;
import com.sau.learningplatform.Service.CourseService;
import com.sau.learningplatform.Service.PostService;
import com.sau.learningplatform.Service.ProjectService;
import com.sau.learningplatform.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import java.security.Principal;
import java.util.List;

@Controller
public class PostController {
    private PostService postService;

    //using just for mocking
    private ProjectService projectService;

    private UserService userService;

    private CourseService courseService;

    public PostController(PostService postService, ProjectService projectService, UserService userService, CourseService courseService) {
        this.postService = postService;
        this.projectService = projectService;
        this.userService = userService;
        this.courseService = courseService;
    }

    @GetMapping("/post/{projectId}")
    public String getPostsByProject(Principal principal, Model model, @PathVariable int projectId,@RequestParam(defaultValue = "0") int pageNo,
                                    @RequestParam(defaultValue = "10") int pageSize){
        String number = principal.getName();
        User user = userService.findByNumber(number);

        model.addAttribute("loggedUser", user);

        ProjectResponse projectResponse=projectService.getResponseById(projectId);

        if (!courseService.isUserRegisteredToCourseInCurrentSemester(user,projectResponse.getCourse().getId()) && !user.getRole().equalsIgnoreCase("admin")){
            return "unauthorized";
        }

        PostPageResponse postPageResponse=postService.getParentPostsAsPostPageResponseByProjectId(projectId,pageNo,pageSize);

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

        ProjectResponse projectResponse=projectService.getResponseById(projectId);
        if (!courseService.isUserRegisteredToCourseInCurrentSemester(user,projectResponse.getCourse().getId()) && !user.getRole().equalsIgnoreCase("admin")){
            return "unauthorized";
        }

        PostPageResponse postPageResponse=postService.searchParentPostsAsPostPageResponseByProjectId(keyword,projectId,pageNo,pageSize);

        model.addAttribute("postPageResponse",postPageResponse);
        model.addAttribute("project",projectResponse);

        return "posts";
    }



    @PostMapping("/post/save")
    public RedirectView savePost(Principal principal
            ,@ModelAttribute PostRequest request
            ,RedirectAttributes redirectAttributes
            ,@RequestParam(defaultValue = "0") int pageNo
            ,@RequestParam(defaultValue = "10") int pageSize
            ,@RequestParam(required = false) String keyword) {

        String number = principal.getName();
        User user = userService.findByNumber(number);
        Project project = projectService.findById(request.getProjectId());

        MessageResponseWithStatus messageResponseWithStatus=postService.savePostRequest(user,project,request);
        redirectAttributes.addFlashAttribute("messageResponseWithStatus", messageResponseWithStatus);

        String redirectUrl;

        if (request.getParentPostId() == null) {
            
            int lastPageNo = postService
                    .getParentPostsAsPostPageResponseByProjectId(request.getProjectId(), pageNo, pageSize)
                    .getTotalPages() - 1;
    
            redirectUrl = "/post/" + request.getProjectId() + "?pageNo=" + lastPageNo;


        } else {
            
            if (keyword != null && !keyword.isEmpty()) {
                redirectUrl = "/post/search/" + request.getProjectId()
                        + "?keyword=" + URLEncoder.encode(keyword, StandardCharsets.UTF_8)
                        + "&pageNo=" + pageNo;
            } else {
                redirectUrl = "/post/" + request.getProjectId() + "?pageNo=" + pageNo;
            }
        }
    
        return new RedirectView(redirectUrl);

    }





}
