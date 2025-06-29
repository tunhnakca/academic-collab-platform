package com.sau.learningplatform.Service;

import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.EntityResponse.MessageResponse;
import com.sau.learningplatform.EntityResponse.MessageResponseWithStatus;
import com.sau.learningplatform.EntityResponse.UserPageResponse;
import com.sau.learningplatform.EntityResponse.UserResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User findById(int id);

    User encodePasswordAndSaveUser(User user);

    User findByNumber(String number);

    void saveAll(List<User> users);

    boolean existsByNumber(String number);

    void deleteById(int id);

    MessageResponseWithStatus updatePassword(User user, String currentPassword, String newPassword);

    void saveUser(User user);

    List<UserResponse> getAllStudents();

    public UserPageResponse getPaginatedUsers(int pageNo, int pageSize);

    UserPageResponse searchUsersPaged(String courseCode, String role,String keyword, int pageNo, int pageSize);
    UserPageResponse getPaginatedUsersByCourseCodeAndRole(String courseCode, String role, int pageNo, int pageSize);

    void sendEmail(String subject, String body,String number);

    MessageResponseWithStatus sendResetPasswordEmail(User user);

    MessageResponseWithStatus resetPassword(User user, String newPassword,String confirmNewPassword);

    Boolean isThereActiveToken(User user);
    public Optional<User> findByNumberOptional(String number);

    Boolean isTokenValid(String token);

    Optional<User> getUserByValidToken(String token);
}