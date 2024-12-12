package com.sau.learningplatform.Service;

import com.sau.learningplatform.Entity.Course;
import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.EntityResponse.CourseResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CourseService {

    List<CourseResponse> getCoursesByUser(User user);

    void save(Course course);

    Course getCourseByCode(String courseCode);

    void addCourseWithStudentsByExcel(String ownerNumber, String courseName, String courseCode,
            MultipartFile studentFile) throws IOException;

    void deleteById(int id);

    Course findById(int courseId);
}