package com.sau.learningplatform.Controller;

import com.sau.learningplatform.EntityResponse.MessageResponse;
import com.sau.learningplatform.Service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class RestController {
    private CourseService courseService;

    private UserService userService;

    private ProjectService projectService;

    private SemesterService semesterService;
    private PostService postService;

    public RestController(CourseService courseService, UserService userService, ProjectService projectService, SemesterService semesterService, PostService postService) {
        this.courseService = courseService;
        this.userService = userService;
        this.projectService = projectService;
        this.semesterService = semesterService;
        this.postService = postService;
    }

    @PutMapping("/courses/remove/user")
    public ResponseEntity<MessageResponse> removeUserFromCourse(@RequestParam String courseCode, @RequestParam String userNumber) {

        return courseService.removeUserFromCourseInActiveSemester(courseCode,userNumber);
    }

    @DeleteMapping("/courses/delete/{courseId}")
    public ResponseEntity<MessageResponse> deleteCourse(@PathVariable("courseId") int courseId) {

        return courseService.deleteByIdAndReturnResponse(courseId);

    }
    

    @DeleteMapping("/projects/delete/{projectId}")
    public ResponseEntity<MessageResponse> deleteProject(@PathVariable("projectId") int id) {

        return projectService.deleteById(id);

    }

    @DeleteMapping("/post/delete")
    public ResponseEntity<MessageResponse> deletePost(@RequestParam int postId) {
        return postService.deleteById(postId);
    }



}
