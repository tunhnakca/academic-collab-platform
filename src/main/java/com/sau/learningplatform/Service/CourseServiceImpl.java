package com.sau.learningplatform.Service;

import com.sau.learningplatform.Entity.Course;
import com.sau.learningplatform.Entity.CourseRegistration;
import com.sau.learningplatform.Entity.Semester;
import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.EntityResponse.CourseResponse;
import com.sau.learningplatform.EntityResponse.MessageResponse;
import com.sau.learningplatform.EntityResponse.MessageResponseWithStatus;
import com.sau.learningplatform.Repository.CourseRegistrationRepository;
import com.sau.learningplatform.Repository.CourseRepository;
import com.sau.learningplatform.Repository.PostRepository;
import com.sau.learningplatform.Repository.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class CourseServiceImpl implements CourseService {
    private CourseRepository courseRepository;
    private UserService userService;
    private CourseRegistrationRepository courseRegistrationRepository;

    private SemesterService semesterService;

    private PostRepository postRepository;
    private ProjectRepository projectRepository;

    public CourseServiceImpl(CourseRepository courseRepository, UserService userService, CourseRegistrationRepository courseRegistrationRepository, SemesterService semesterService, PostRepository postRepository, ProjectRepository projectRepository) {
        this.courseRepository = courseRepository;
        this.userService = userService;
        this.courseRegistrationRepository = courseRegistrationRepository;
        this.semesterService = semesterService;
        this.postRepository = postRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public List<CourseResponse> getActiveCourseResponsesByUser(User user) {

        List<CourseRegistration>courseRegistrations=courseRegistrationRepository.findByUserIdAndSemesterId(user.getId(), semesterService.getCurrentSemester().getId());

        List<Course> courses = courseRegistrations.stream().map(CourseRegistration::getCourse).toList();

        if (courses.isEmpty()) {
            log.warn("no active courses found for: {} !", user.getName());
        }
        return courses.stream().map(this::courseToResponse).toList();
    }

    @Override
    public void save(Course course) {
        courseRepository.save(course);
    }

    @Override
    public CourseResponse getCourseResponseByCode(String courseCode) {
        Optional<Course>course = courseRepository.findByCode(courseCode);

        if (course.isEmpty()) {
            throw new RuntimeException("there is no course with given code !");
        }

        return courseToResponse(course.get());
    }

    @Override
    public MessageResponseWithStatus createCourseWithUsers(String ownerNumber, String courseName, String courseCode,
                                                           MultipartFile studentFile) throws IOException {

        if (courseRepository.existsByCode(courseCode)) {
            log.warn("The course with given code is already exists!");

            return new MessageResponseWithStatus("The course with given code is already exists!", false);

        }
        if (!semesterService.isThereActiveSemester()) {
            log.warn("The course with given code is already exists!");

            return new MessageResponseWithStatus("The semester has not started yet!", false);

        }

        // Parse the uploaded Excel file
        List<User> users = getStudentsFromExcelAndCreateNonExists(studentFile);

        if (users.isEmpty()){
            return new MessageResponseWithStatus("There was a problem with excel! please reconsider the example given", false);
        }

        User owner = userService.findByNumber(ownerNumber);
        String ownerName = owner.getName() + " ".concat(owner.getSurname());

        // get a course, save and associate students with it
        Course course = new Course(courseName, ownerName, courseCode);

        userService.saveAll(users);

        users.add(owner);

        createRegistriesForUsers(users,course,semesterService.getCurrentSemester());

        log.info("course registries have been saved !");
        return new MessageResponseWithStatus("Course has been added successfully", true);

    }




    @Override
    public void deleteById(int id) {
        courseRepository.deleteById(id);
    }

    @Override
    public Course findById(int courseId) {
        Optional<Course> course = courseRepository.findById(courseId);

        if (course.isEmpty()) {
            log.error("there is no course with given id !");
        }

        return course.get();
    }

    @Override
    public List<CourseResponse> getAllCourseResponses() {
        return courseRepository.findAll().stream().map(this::courseToResponse).toList();
    }

    @Override
    public Course getByCode(String code) {
        Optional<Course>course=courseRepository.findByCode(code);

        if (course.isEmpty()){
            log.error("there is no course with given code!");
        }
        return course.get();

    }

    @Override
    public MessageResponseWithStatus addUsersToCourse(List<User> users, String courseCode) {

        Optional<Course> course=courseRepository.findByCode(courseCode);

        if (course.isEmpty()){
            log.error("Invalid course code given!");
            return new MessageResponseWithStatus("Invalid course code given!",false);
        }

        createRegistriesForUsers(users,course.get(),semesterService.getCurrentSemester());

        return new MessageResponseWithStatus("Users have been saved to course!",true);
    }

    @Override
    public ResponseEntity<MessageResponse> removeUserFromCourseInActiveSemester(String courseCode, String userNumber) {
        Optional<Course> course=courseRepository.findByCode(courseCode);
        User user=userService.findByNumber(userNumber);
        if (course.isEmpty()){
            log.error("There is no course with given code!");
            return new ResponseEntity<>(new MessageResponse("There is no course with given code!"), HttpStatus.NOT_FOUND);
        }
        Optional<CourseRegistration>courseRegistration=courseRegistrationRepository.findByCourseIdAndUserId(course.get().getId(), user.getId());
        if(courseRegistration.isEmpty()){
            log.error("there is no registry for given user and course");
            return new ResponseEntity<>(new MessageResponse("there is no registry for given user and course"), HttpStatus.NOT_FOUND);
        }
        courseRegistrationRepository.deleteById(courseRegistration.get().getId());

        log.info("user has been removed successfully from course");
        return new ResponseEntity<>(new MessageResponse("User has been removed successfully from course"), HttpStatus.OK);
    }

    @Override
    public void transferInstructorsAndAdminsToNewSemester(Semester previousSemester, Semester newSemester) {
        List<CourseRegistration> adminAndInstructorRegistries = new ArrayList<>(courseRegistrationRepository.findByUserRoleIgnoreCaseAndSemester("INSTRUCTOR", previousSemester));
        adminAndInstructorRegistries.addAll(courseRegistrationRepository.findByUserRoleIgnoreCaseAndSemester("ADMIN",previousSemester));

        for(CourseRegistration oldRegistry:adminAndInstructorRegistries){

            CourseRegistration newRegistry=new CourseRegistration();
            newRegistry.setCourse(oldRegistry.getCourse());
            newRegistry.setUser(oldRegistry.getUser());
            newRegistry.setSemester(newSemester);
            
            courseRegistrationRepository.save(newRegistry);
        }

        postRepository.deleteAll();
        log.info("past posts have been deleted");
    }

    @Override
    public ResponseEntity<MessageResponse> deleteByIdAndReturnResponse(int courseId) {

        try {
            courseRepository.deleteById(courseId);
            return new ResponseEntity<>(new MessageResponse("Course has been deleted successfully!"),(HttpStatus.OK));
        } catch (Exception e) {
            log.info("Failed to delete course! {}",e.getMessage());
            return new ResponseEntity<>(new MessageResponse("Course could not be deleted."),(HttpStatus.BAD_REQUEST));
        }

    }



    public List<User> getStudentsFromExcelAndCreateNonExists(MultipartFile file) throws IOException {
        List<User> students = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

            for (int rowIndex = 1; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) { // Skip header row
                Row row = sheet.getRow(rowIndex);
                if (row != null) {
                    String name = row.getCell(0).getStringCellValue();
                    String surname = row.getCell(1).getStringCellValue();
                    String number = row.getCell(2).getStringCellValue();

                    if (number.isEmpty()||surname.isEmpty()||name.isEmpty()){
                        continue;
                    }

                    number=number.toUpperCase();

                    User student;

                    // if user already exists just get it
                    if (userService.existsByNumber(number)) {
                        student = userService.findByNumber(number);
                    }
                    // if it's not exists create a new user object
                    else {
                        student = new User(number, name, surname, number, "student");
                        userService.encodePasswordAndSaveUser(student);
                    }
                    students.add(student);
                }
            }
        } catch (Exception e) {
            log.warn(e.getMessage());

            return new ArrayList<>();
        }

        return students;
    }

    @Override
    public MessageResponseWithStatus addStudentsToCourseByExcelAndSaveNonExistingStudents(MultipartFile file, String courseCode) throws IOException {
        List<User>users=getStudentsFromExcelAndCreateNonExists(file);

        if (users.isEmpty()){
            return new MessageResponseWithStatus("There was a problem with excel! please reconsider the example given", false);
        }

        Optional<Course>course=courseRepository.findByCode(courseCode);
        if (course.isEmpty()){
            log.error("invalid courseCode!");
            return new MessageResponseWithStatus("There was a problem with Course!", false);
        }
        if (!semesterService.isThereActiveSemester()) {
            log.warn("The course with given code is already exists!");

            return new MessageResponseWithStatus("The semester has not started yet!", false);
        }

        userService.saveAll(users);

        createRegistriesForUsers(users,course.get(),semesterService.getCurrentSemester());

        log.info("course registries have been saved !");
        return new MessageResponseWithStatus("Users have been added successfully", true);

    }

    @Override
    public boolean isUserRegisteredToCourseInCurrentSemester(User user, int courseId) {
        Optional<Course> course=courseRepository.findById(courseId);
        if (course.isEmpty()){
            log.error("there is no course with given id");
        }
        return courseRegistrationRepository.existsByUserAndCourseAndSemester(user,course.get(), semesterService.getCurrentSemester());
    }

    private void createRegistriesForUsers(List<User>users,Course course,Semester currentSemester) {

        for (User user : users) {
            if (!courseRegistrationRepository.existsByUserAndCourseAndSemester(user, course, currentSemester)) {
                CourseRegistration courseRegistration = new CourseRegistration();
                courseRegistration.setCourse(course);
                courseRegistration.setUser(user);
                courseRegistration.setSemester(semesterService.getCurrentSemester());
                courseRegistrationRepository.save(courseRegistration);
            }
        }
    }


    @Override
    public MessageResponseWithStatus addStudentToCourseAndSaveNonExistingStudent(User student, String courseCode) {

        Optional<Course> course=courseRepository.findByCode(courseCode);

        if (course.isEmpty()){
            return new MessageResponseWithStatus(("There is no such a course with given code!"), false);
        }
        if (!semesterService.isThereActiveSemester()) {
            log.warn("The course with given code is already exists!");

            return new MessageResponseWithStatus("The semester has not started yet!", false);
        }
        student.setNumber(student.getNumber().toUpperCase());
        User user=student;

        if (userService.existsByNumber(student.getNumber())) {

            user=userService.findByNumber(student.getNumber());
            if (courseRegistrationRepository.existsByUserAndCourseAndSemester(user, course.get(), semesterService.getCurrentSemester())) {
                log.warn("Student is already registered to this course in current semester");
                return new MessageResponseWithStatus("Student is already registered to this course", false);
            }
        }
        else {
            user.setPassword(user.getNumber());
            user.setRole("student");
            userService.encodePasswordAndSaveUser(user);
        }

        CourseRegistration courseRegistration =new CourseRegistration();
        courseRegistration.setUser(user);
        courseRegistration.setCourse(course.get());
        courseRegistration.setSemester(semesterService.getCurrentSemester());

        courseRegistrationRepository.save(courseRegistration);

        log.info("a new course registry has been saved for {}",course.get().getTitle());
        return new MessageResponseWithStatus(("New registry has been saved successfully"), true);
    }



    private CourseResponse courseToResponse(Course course) {

        return CourseResponse
                .builder()
                .id(course.getId())
                .code(course.getCode())
                .owner(course.getOwner())
                .title(course.getTitle())
                .openProjectCount(projectRepository.countByCourseIdAndDateEndAfter(course.getId(),LocalDateTime.now()))
                .closedProjectCount(projectRepository.countByCourseIdAndDateEndBefore(course.getId(),LocalDateTime.now()))
                .studentCount(courseRegistrationRepository.countByCourseIdAndUserRoleIgnoreCaseAndSemester(course.getId(),"student",semesterService.getCurrentSemester()))
                .build();

    }
}