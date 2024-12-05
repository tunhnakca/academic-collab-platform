package com.sau.learningplatform.Service;

import com.sau.learningplatform.Entity.Course;
import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.EntityResponse.CourseResponse;

import java.util.List;

public interface CourseService {

    List<CourseResponse> getCoursesByUser(User user);
}
