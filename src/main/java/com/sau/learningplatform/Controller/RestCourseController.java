package com.sau.learningplatform.Controller;

import com.sau.learningplatform.EntityResponse.MessageResponse;
import com.sau.learningplatform.Service.CourseService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RestCourseController {
    private CourseService courseService;

    public RestCourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PutMapping("/courses/remove/user")
    public MessageResponse removeUserFromCourse(@RequestParam String courseCode, @RequestParam String userNumber) {

        return courseService.removeUserFromCourseInActiveSemester(courseCode,userNumber);
    }

}
