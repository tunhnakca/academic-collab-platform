package com.sau.learningplatform.Service;

import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.EntityResponse.MessageResponse;
import com.sau.learningplatform.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
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



    @Override
    public MessageResponse updatePassword(User user, String currentPassword, String newPassword) {

        if (!encoder.matches(currentPassword, user.getPassword())){

            log.warn("Incorrect password, change request has been denied!");

            return new MessageResponse( "incorrect current password!", HttpStatus.UNAUTHORIZED);

        }

        if (newPassword.equals(user.getNumber())){

            log.warn("Your new password cannot be same as your number!");

            return new MessageResponse( "new password cannot be same as your number!", HttpStatus.BAD_REQUEST);

        }

        user.setPassword(encoder.encode(newPassword));

        userRepository.save(user);

        log.info("password has been updated successfully! ");

        return new MessageResponse( "Your password has been updated successfully!", HttpStatus.OK);


    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }


}
