package com.sau.learningplatform.Service;

import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.EntityResponse.MessageResponse;
import com.sau.learningplatform.EntityResponse.MessageResponseWithStatus;
import com.sau.learningplatform.EntityResponse.UserPageResponse;
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

    MessageResponseWithStatus updatePassword(User user, String currentPassword, String newPassword);

    void saveUser(User user);

    List<UserResponse> getAllStudents();

    public UserPageResponse getPaginatedUsers(int pageNo, int pageSize);


    List<UserResponse> getUsersByCourseCodeAndRole(String courseCode,String role);
}