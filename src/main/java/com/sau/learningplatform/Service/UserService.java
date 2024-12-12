package com.sau.learningplatform.Service;

import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.EntityResponse.MessageResponse;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface UserService {
    User findById(int id);
    void register(User user);
    User findByNumber(String number);

    void saveAll(List<User>users);

    boolean existsByNumber(String number);

    void deleteById(int id);

    MessageResponse updatePassword(User user, String currentPassword, String newPassword);
}
