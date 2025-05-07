package com.sau.learningplatform.Service;

import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.EntityResponse.MessageResponse;
import com.sau.learningplatform.EntityResponse.UserResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    User findById(int id);

    void encodePasswordAndSaveUser(User user);

    User findByNumber(String number);

    void saveAll(List<User> users);

    boolean existsByNumber(String number);

    void deleteById(int id);

    ResponseEntity<MessageResponse> updatePassword(User user, String currentPassword, String newPassword);

    void saveUser(User user);

    List<UserResponse> getAllStudents();


    List<UserResponse> getUsersByCourseCodeAndRole(String courseCode,String role);
}