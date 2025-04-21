package com.sau.learningplatform.Service;

import com.sau.learningplatform.Entity.Course;
import com.sau.learningplatform.Entity.CourseRegistration;
import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.EntityResponse.CourseResponse;
import com.sau.learningplatform.Repository.CourseRegistrationRepository;
import com.sau.learningplatform.Repository.CourseRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CourseServiceImpl implements CourseService {
    private CourseRepository courseRepository;
    private UserService userService;
    private CourseRegistrationRepository courseRegistrationRepository;

    private SemesterService semesterService;

    public CourseServiceImpl(CourseRepository courseRepository, UserService userService, CourseRegistrationRepository courseRegistrationRepository, SemesterService semesterService) {
        this.courseRepository = courseRepository;
        this.userService = userService;
        this.courseRegistrationRepository = courseRegistrationRepository;
        this.semesterService = semesterService;
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
    public void createCourseWithUsers(String ownerNumber, String courseName, String courseCode,
                                      MultipartFile studentFile) throws IOException {

        if (courseRepository.existsByCode(courseCode)) {
            throw new RuntimeException("The course with given code is already exists!");
        }

        // Parse the uploaded Excel file
        List<User> users = getStudentsFromExcelAndCreateNonExists(studentFile);

        User owner = userService.findByNumber(ownerNumber);
        String ownerName = owner.getName() + " ".concat(owner.getSurname());

        // get a course, save and associate students with it
        Course course = new Course(courseName, ownerName, courseCode);

        users.add(owner);

        for(User user:users){
            CourseRegistration courseRegistration=new CourseRegistration();
            courseRegistration.setCourse(course);
            courseRegistration.setUser(user);
            courseRegistration.setSemester(semesterService.getCurrentSemester());
            courseRegistrationRepository.save(courseRegistration);
        }

        log.info("course registries have been saved !");

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
    public void removeUserFromCourseInActiveSemester(String courseCode, String userNumber) {
        Optional<Course> course=courseRepository.findByCode(courseCode);
        User user=userService.findByNumber(userNumber);
        if (course.isEmpty()){
            throw new RuntimeException("There is no course with given code!");
        }
        Optional<CourseRegistration>courseRegistration=courseRegistrationRepository.findByCourseIdAndUserId(course.get().getId(), user.getId());
        if(courseRegistration.isEmpty()){
            throw new RuntimeException("there is no registry for given user and course");
        }
        courseRegistrationRepository.deleteById(courseRegistration.get().getId());

        log.info("user has been removed successfully from course");
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

                    User student;

                    // if user already exists just get it
                    if (userService.existsByNumber(number)) {
                        student = userService.findByNumber(number);
                    }
                    // if it's not exists create a new user
                    else {
                        student = new User(number, name, surname, number, "student");
                        //userService.register(student);
                    }
                    students.add(student);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }


        return students;
    }

    @Override
    public void addStudentToCourseAndSaveNonExistingStudent(User student, String courseCode) {

        Optional<Course> course=courseRepository.findByCode(courseCode);

        if (course.isEmpty()){
            throw new RuntimeException("there is no such a course with given code!");
        }

        User user=student;

        if (userService.existsByNumber(student.getNumber())) {

            user=userService.findByNumber(student.getNumber());

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

    }


    private CourseResponse courseToResponse(Course course) {

        return CourseResponse.builder().id(course.getId()).code(course.getCode()).owner(course.getOwner())
                .title(course.getTitle()).build();

    }
}