package com.sau.learningplatform.Service;

import com.sau.learningplatform.Entity.CourseRegistration;
import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.EntityResponse.MessageResponseWithStatus;
import com.sau.learningplatform.EntityResponse.UserPageResponse;
import com.sau.learningplatform.EntityResponse.UserResponse;
import com.sau.learningplatform.Repository.CourseRegistrationRepository;
import com.sau.learningplatform.Repository.CourseRepository;
import com.sau.learningplatform.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;


    private SemesterService semesterService;

    private CourseRegistrationRepository courseRegistrationRepository;

    final private BCryptPasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository, SemesterService semesterService, CourseRegistrationRepository courseRegistrationRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.semesterService = semesterService;
        this.courseRegistrationRepository = courseRegistrationRepository;
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
    public void encodePasswordAndSaveUser(User user) {
        String hashedPassword = encoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
        log.info("New user has been registered !");
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
    public MessageResponseWithStatus updatePassword(User user, String currentPassword, String newPassword) {

        if (!encoder.matches(currentPassword, user.getPassword())) {

            log.warn("Incorrect password, change request has been denied!");
            return new MessageResponseWithStatus("Incorrect current password!", false);

        }

        if (newPassword.equals(user.getNumber())) {

            log.warn("Your new password cannot be same as your number!");
            return new MessageResponseWithStatus("New password cannot be same as your number!", false);


        }

        user.setPassword(encoder.encode(newPassword));

        userRepository.save(user);

        log.info("password has been updated successfully! ");
        return new MessageResponseWithStatus("Your password has been updated successfully!", true);


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
    public UserPageResponse getPaginatedUsersByCourseCodeAndRole(String courseCode, String role, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<CourseRegistration> registrations = courseRegistrationRepository
                .findByCourseCodeAndUserRoleAndSemester(courseCode, role, semesterService.getCurrentSemester(), pageable);

        List<UserResponse> userResponses = registrations.getContent().stream()
                .map(CourseRegistration::getUser)
                .map(this::mapToUserResponse)
                .toList();

        if(userResponses.isEmpty()){
            log.info("paginated users are empty.");
        }

        UserPageResponse response = new UserPageResponse();
        response.setUsers(userResponses);
        response.setPageSize(pageSize);
        response.setPageNo(pageNo);
        response.setTotalPages(registrations.getTotalPages());


        return response;
    }


    @Override
    public UserPageResponse getPaginatedUsers(int pageNo, int pageSize) {
        Pageable pageable= PageRequest.of(pageNo,pageSize);
        Page<User> users=userRepository.findAll(pageable);

        return usersToPageResponse(pageNo,pageSize,users);

    }

    private UserPageResponse usersToPageResponse(int pageNo, int pageSize, Page<User>users){
        List<User>productList=users.getContent();
        List<UserResponse>userResponses=productList.stream().map(this::mapToUserResponse).toList();

        UserPageResponse userPageResponse=new UserPageResponse();

        userPageResponse.setUsers(userResponses);
        userPageResponse.setPageSize(pageSize);
        userPageResponse.setPageNo(pageNo);
        userPageResponse.setTotalPages(users.getTotalPages());

        return userPageResponse;

    }


}