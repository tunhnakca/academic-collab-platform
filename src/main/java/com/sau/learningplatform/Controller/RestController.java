package com.sau.learningplatform.Controller;

import com.sau.learningplatform.Entity.Project;
import com.sau.learningplatform.Entity.Semester;
import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.EntityResponse.MessageResponse;
import com.sau.learningplatform.EntityResponse.SemesterResponse;
import com.sau.learningplatform.Service.CourseService;

import com.sau.learningplatform.Service.ProjectService;
import com.sau.learningplatform.Service.SemesterService;
import com.sau.learningplatform.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.security.Principal;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class RestController {
    private CourseService courseService;

    private UserService userService;

    private ProjectService projectService;

    private SemesterService semesterService;

    public RestController(CourseService courseService, UserService userService, ProjectService projectService, SemesterService semesterService) {
        this.courseService = courseService;
        this.userService = userService;
        this.projectService = projectService;
        this.semesterService = semesterService;
    }

    @PutMapping("/courses/remove/user")
    public ResponseEntity<MessageResponse> removeUserFromCourse(@RequestParam String courseCode, @RequestParam String userNumber) {

        return courseService.removeUserFromCourseInActiveSemester(courseCode,userNumber);
    }

    @PostMapping("/courses/delete/{courseId}")
    public ResponseEntity<MessageResponse> deleteCourse(@PathVariable("courseId") int courseId) {

        return courseService.deleteByIdAndReturnResponse(courseId);
        //location.reload lazım yok ise

    }

    @PostMapping("/courses/add")
    public ResponseEntity<MessageResponse> addCourse(Principal principal,
                            @RequestParam("courseName") String courseName,
                            @RequestParam("courseCode") String courseCode,
                            @RequestParam("file") MultipartFile studentFile
                            ) throws IOException {

        return courseService.createCourseWithUsers(principal.getName(), courseName, courseCode, studentFile);

        //location.reload lazım yok ise
    }

    @PostMapping("/password/change")
    public ResponseEntity<MessageResponse> changePassword(Principal principal,
                                       @RequestParam("currentPassword") String currentPassword,
                                       @RequestParam("newPassword") String newPassword) {
        String number = principal.getName();
        User user = userService.findByNumber(number);

        return userService.updatePassword(user,currentPassword, newPassword);



        //location.reload lazım yok ise
    }

    @PostMapping("/projects/add")
    public ResponseEntity<MessageResponse> saveNewProject(@ModelAttribute Project project, @RequestParam("courseCode") String courseCode) {


        return projectService.saveProjectToCourseWithCode(project,courseCode);

        //location.reload lazım yok ise

        //return "redirect:/projects?courseCode=" + courseCode;
    }

    @DeleteMapping("/projects/delete/{projectId}")
    public ResponseEntity<MessageResponse> deleteProject(@PathVariable("projectId") int id) {

        return projectService.deleteById(id);


        //location.reload lazım yok ise
    }





}
