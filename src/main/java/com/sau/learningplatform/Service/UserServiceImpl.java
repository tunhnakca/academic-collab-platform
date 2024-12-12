package com.sau.learningplatform.Service;

import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.Repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public User findById(int id) {

        Optional<User> result=userRepository.findById(id);

        if (result.isEmpty()){
            throw new RuntimeException("User not found with given id !");
        }

        return result.get();
    }

    @Override
    public void register(User user) {
        String hashedPassword=encoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
    }

    @Override
    public User findByNumber(String number) {

        Optional<User>result=userRepository.findByNumber(number);

        if (result.isEmpty()){
            throw new RuntimeException("User not found with given number !");
        }

        return result.get();

    }


    @Override
    public void saveAll(List<User> users) {
        userRepository.saveAll(users);
    }

    @Override
    public boolean existsByNumber(String number) {
        return userRepository.existsByNumber(number);
    }

    @Override
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }


}
