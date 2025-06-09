package com.sau.learningplatform.Service;

import com.sau.learningplatform.Entity.Course;
import com.sau.learningplatform.Entity.Semester;
import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.EntityResponse.CourseResponse;
import com.sau.learningplatform.EntityResponse.MessageResponse;
import com.sau.learningplatform.EntityResponse.MessageResponseWithStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CourseService {

    List<CourseResponse> getActiveCourseResponsesByUser(User user);

    void save(Course course);

    CourseResponse getCourseResponseByCode(String courseCode);

    MessageResponseWithStatus createCourseWithUsers(String ownerNumber, String courseName, String courseCode,
                                                    MultipartFile studentFile) throws IOException;

    void deleteById(int id);

    Course findById(int courseId);

    List<CourseResponse>getAllCourseResponses();

    Course getByCode(String code);

    MessageResponseWithStatus addUsersToCourse(List<User> users,String courseCode);

    MessageResponseWithStatus addStudentToCourseAndSaveNonExistingStudent(User user, String courseCode);
    ResponseEntity<MessageResponse> removeUserFromCourseInActiveSemester(String courseCode, String userNumber);

    void transferInstructorsAndAdminsToNewSemester(Semester closestPastSemester, Semester newSemester);

    ResponseEntity<MessageResponse> deleteByIdAndReturnResponse(int courseId);

    MessageResponseWithStatus addStudentsToCourseByExcelAndSaveNonExistingStudents(MultipartFile file, String courseCode) throws IOException;
}