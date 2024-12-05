package com.sau.learningplatform.Service;

import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findById(int id) {

        Optional<User> result=userRepository.findById(id);

        if (result.isEmpty()){
            throw new RuntimeException("User not found with given id !");
        }

        return result.get();
    }
}
