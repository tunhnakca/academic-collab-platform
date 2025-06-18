package com.sau.learningplatform.Service;

import com.sau.learningplatform.Entity.CourseRegistration;
import com.sau.learningplatform.Entity.Semester;
import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.EntityResponse.MessageResponseWithStatus;
import com.sau.learningplatform.EntityResponse.UserPageResponse;
import com.sau.learningplatform.EntityResponse.UserResponse;
import com.sau.learningplatform.Repository.CourseRegistrationRepository;
import com.sau.learningplatform.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    
    private SemesterService semesterService;

    private CourseRegistrationRepository courseRegistrationRepository;

    final private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;


    public UserServiceImpl(UserRepository userRepository, SemesterService semesterService, CourseRegistrationRepository courseRegistrationRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.semesterService = semesterService;
        this.courseRegistrationRepository = courseRegistrationRepository;
        this.passwordEncoder = passwordEncoder;
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
        String hashedPassword = passwordEncoder.encode(user.getPassword());
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
    public Optional<User> findByNumberOptional(String number) {

        return userRepository.findByNumber(number);
    }

    @Override
    public UserPageResponse searchUsersPaged(String courseCode,String role, String keyword, int pageNo, int pageSize) {
        Semester currentSemester = semesterService.getCurrentSemester();
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Page<User> userPage = userRepository.searchUsersByCourseAndRoleAndNameOrSurnamePaged(
                courseCode, currentSemester, role,keyword, pageable);

        return usersToPageResponse(pageNo, pageSize, userPage);
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

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {

            log.warn("Incorrect password, change request has been denied!");
            return new MessageResponseWithStatus("Incorrect current password!", false);
        }

        if (newPassword.equalsIgnoreCase(user.getNumber())) {

            log.warn("Your new password cannot be same as your number!");
            return new MessageResponseWithStatus("New password cannot be same as your number!", false);
        }

        user.setPassword(passwordEncoder.encode(newPassword));

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
                .collect(Collectors.toList());

        if(userResponses.isEmpty()){
            log.info("paginated users are empty.");
        }
        else {
            userResponses.sort(Comparator.comparing(UserResponse::getName));

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


    public MessageResponseWithStatus sendResetPasswordEmail(User user) {
    try {
        // create token
        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(30); // valid for 30 minutes

        // update user
        user.setResetToken(token);
        user.setTokenExpiryDate(expiryDate);
        userRepository.save(user);

        //create email
        String subject = "Password Reset Request";
        String resetLink = "http://localhost:8080/reset-password?token=" + token;

        String body = "Hi " + user.getName() + ",\n\n"
                + "To reset your password, click the link below:\n"
                + resetLink + "\n\n"
                + "If you did not request a password reset, please ignore this email.\n\n"
                + "Best regards,\nYour App Team";

        sendEmail(subject, body, user.getNumber());

        log.info("Email sent successfully to {}", user.getName());

        return new MessageResponseWithStatus("Password reset email sent successfully.\n" +
                "Please check your inbox to continue.",true);
    }
    catch (Exception e){
        log.warn("Failed to send email to {}: {}", user.getName(), e.getMessage());
        return new MessageResponseWithStatus("An error occurred while sending the password reset email. Please contact support if the problem persists. ",false);
    }

    }

    public void sendEmail(String subject, String body,String number){

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(number.toLowerCase()+"@sakarya.edu.tr");
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(fromEmail);
        mailSender.send(message);

    }

    public Boolean isThereActiveToken(User user){
        if (user.getTokenExpiryDate()!=null && user.getTokenExpiryDate().isAfter(LocalDateTime.now())){
            return true;
        }
        return false;
    }


    public Boolean isTokenValid(String token){
        Optional<User> user = userRepository.findByResetToken(token);

        if (user.isEmpty()){
            log.info("There is no user found with given token");
            return false;
        }

        if (user.get().getTokenExpiryDate().isBefore(LocalDateTime.now())) {
            log.info("Expired token.");
            return false;
        }
        return true;
    }

    @Override
    public Optional<User> getUserByValidToken(String token) {
        if(!isTokenValid(token)){
            return Optional.empty();
        }
        Optional<User>user= userRepository.findByResetToken(token);
        if (user.isEmpty()){
            log.warn("any user not found with given token");
        }
        return user;
    }

    public MessageResponseWithStatus resetPassword(User user, String newPassword) {

        if (newPassword.equalsIgnoreCase(user.getNumber())) {
            log.warn("Your new password cannot be same as your number!");
            return new MessageResponseWithStatus("New password cannot be same as your number!", false);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setTokenExpiryDate(null);
        userRepository.save(user);

        return new MessageResponseWithStatus("Your password has been updated successfully!", true);
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