package com.sau.learningplatform.Controller;

import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.EntityResponse.CourseResponse;
import com.sau.learningplatform.EntityResponse.MessageResponseWithStatus;
import com.sau.learningplatform.Service.CourseService;
import com.sau.learningplatform.Service.SemesterService;
import com.sau.learningplatform.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@Slf4j
public class CourseController {

    private CourseService courseService;

    private UserService userService;

    private SemesterService semesterService;

    public CourseController(CourseService courseService, UserService userService, SemesterService semesterService) {
        this.courseService = courseService;
        this.userService = userService;
        this.semesterService = semesterService;
    }

    @GetMapping("/courses")
    public String homepage(Principal principal, Model model) {
        String number = principal.getName();
        User user = userService.findByNumber(number);
        List<CourseResponse> courses;
        if (user.getRole().equalsIgnoreCase("admin")) {
            if(!semesterService.isThereActiveSemester()){
                return "redirect:/semester/add";
            }
            courses = courseService.getAllCourseResponses();
        } else {
            courses = courseService.getActiveCourseResponsesByUser(user);
        }
        model.addAttribute("courses", courses);
        model.addAttribute("loggedUser", user);

        return "courses";
    }

    @GetMapping("/courses/add")
    public String addCoursePage(Principal principal, Model model) {
        String number = principal.getName();
        User user = userService.findByNumber(number);
        model.addAttribute("loggedUser", user);
        return "add-course";

    }

    @PostMapping("/courses/add")
    public RedirectView addCourse(Principal principal,RedirectAttributes redirectAttributes,
                                                     @RequestParam("courseName") String courseName,
                                                     @RequestParam("courseCode") String courseCode,
                                                     @RequestParam("file") MultipartFile studentFile
    ) throws IOException {

         MessageResponseWithStatus messageResponseWithStatus= courseService.createCourseWithUsers(principal.getName(), courseName, courseCode, studentFile);
         redirectAttributes.addFlashAttribute("messageResponseWithStatus", messageResponseWithStatus);


        //return "redirect:/courses";
        return new RedirectView("/courses/add");
    }

    @PostMapping("/course/add/student")
    public RedirectView addStudentToCourse(RedirectAttributes redirectAttributes,
                                  @RequestParam("courseCode") String courseCode,
                                  @ModelAttribute("addStudent")User student)
    {

        MessageResponseWithStatus messageResponseWithStatus=courseService.addStudentToCourseAndSaveNonExistingStudent(student,courseCode);
        redirectAttributes.addFlashAttribute("messageResponseWithStatus", messageResponseWithStatus);

        return new RedirectView("/student/list?courseCode="+courseCode);

    }

    @PostMapping("/course/add/student/excel")
    public RedirectView addStudentsByExcelToCourse(RedirectAttributes redirectAttributes,
                                           @RequestParam("courseCode") String courseCode,
                                           @RequestParam("fileUserList") MultipartFile file) throws IOException {

        MessageResponseWithStatus messageResponseWithStatus=courseService.addStudentsToCourseByExcelAndSaveNonExistingStudents(file,courseCode);
        redirectAttributes.addFlashAttribute("messageResponseWithStatus", messageResponseWithStatus);

        return new RedirectView("/student/list?courseCode="+courseCode);

    }




}
