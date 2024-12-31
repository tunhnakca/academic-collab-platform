package com.sau.learningplatform.Service;

import com.sau.learningplatform.Entity.Course;
import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.EntityResponse.MessageResponse;
import com.sau.learningplatform.EntityResponse.UserResponse;
import com.sau.learningplatform.Repository.CourseRepository;
import com.sau.learningplatform.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    private CourseRepository courseRepository;

    private BCryptPasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository, CourseRepository courseRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.encoder = encoder;
    }

    @Override
    public User findById(int id) {

        Optional<User> result = userRepository.findById(id);

        if (result.isEmpty()) {
            throw new RuntimeException("User not found with given id !");
        }

        return result.get();
    }

    @Override
    public void register(User user) {
        String hashedPassword = encoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
    }

    @Override
    public User findByNumber(String number) {

        Optional<User> result = userRepository.findByNumber(number);

        if (result.isEmpty()) {
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

        if (!encoder.matches(currentPassword, user.getPassword())) {

            log.warn("Incorrect password, change request has been denied!");

            return new MessageResponse("incorrect current password!", HttpStatus.UNAUTHORIZED);

        }

        if (newPassword.equals(user.getNumber())) {

            log.warn("Your new password cannot be same as your number!");

            return new MessageResponse("new password cannot be same as your number!", HttpStatus.BAD_REQUEST);

        }

        user.setPassword(encoder.encode(newPassword));

        userRepository.save(user);

        log.info("password has been updated successfully! ");

        return new MessageResponse("Your password has been updated successfully!", HttpStatus.OK);

    }

    private UserResponse mapToUserResponse(User user){

        return UserResponse
                .builder()
                .id(user.getId())
                .name(user.getName())
                .role(user.getRole())
                .surname(user.getSurname())
                .number(user.getNumber())
                .build();

    }




    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public List<UserResponse> getAllStudents() {
        List<User>students=userRepository.findByRoleIgnoreCase("student");
        if (students.isEmpty()){
            log.info("There is no any student!");
        }
        return students.stream().map(this::mapToUserResponse).toList();
    }

    @Override
    public void addStudentToCourseAndSaveNonExistingStudent(User student, String courseCode) {

        Optional<Course> course=courseRepository.findByCode(courseCode);

        if (course.isEmpty()){
            throw new RuntimeException("there is no such a course with given code!");
        }


        if (userRepository.existsByNumber(student.getNumber())){

            User user=userRepository.findByNumber(student.getNumber()).get();
            user.addCourse(course.get());

            userRepository.save(user);

            log.info("Student added to course {}",course.get().getTitle());

        }
        else{
            String upperCasedNumber=student.getNumber().toUpperCase();
            student.setNumber(upperCasedNumber);
            student.setPassword(encoder.encode(student.getNumber()));
            student.addCourse(course.get());
            student.setRole("student");

            userRepository.save(student);

            log.info("New student has been saved with a course! ");
        }

    }


}