package com.sau.learningplatform.Service;

import com.sau.learningplatform.Entity.Course;
import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.EntityResponse.CourseResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CourseService {

    List<CourseResponse> getActiveCourseResponsesByUser(User user);

    void save(Course course);

    CourseResponse getCourseResponseByCode(String courseCode);

    void createCourseWithUsers(String ownerNumber, String courseName, String courseCode,
                               MultipartFile studentFile) throws IOException;

    void deleteById(int id);

    Course findById(int courseId);

    List<CourseResponse>getAllCourseResponses();

    Course getByCode(String code);

    void addStudentToCourseAndSaveNonExistingStudent(User user, String courseCode);
    void removeUserFromCourseInActiveSemester(String courseCode, String userNumber);
}