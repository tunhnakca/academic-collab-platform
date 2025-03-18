package com.sau.learningplatform.Service;

import com.sau.learningplatform.Entity.Course;
import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.EntityResponse.CourseResponse;
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

@Service
@Slf4j
public class CourseServiceImpl implements CourseService {
    private CourseRepository courseRepository;
    private UserService userService;

    public CourseServiceImpl(CourseRepository courseRepository, UserService userService) {
        this.courseRepository = courseRepository;
        this.userService = userService;
    }

    @Override
    public List<CourseResponse> getCoursesByUser(User user) {

        List<Course> courses = courseRepository.findCoursesByUsers(user);

        if (courses.isEmpty()) {
            log.warn("no courses found for: {} !", user.getName());
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
    public void addCourseWithStudentsByExcel(String ownerNumber, String courseName, String courseCode,
            MultipartFile studentFile) throws IOException {

        if (courseRepository.existsByCode(courseCode)) {
            throw new RuntimeException("The course with given code is already exists!");
        }

        // Parse the uploaded Excel file
        List<User> students = saveStudentsByFile(studentFile);

        User owner = userService.findByNumber(ownerNumber);
        String ownerName = owner.getName() + " ".concat(owner.getSurname());

        // get a course, save and associate students with it
        Course course = new Course(courseName, ownerName, courseCode);

        students.forEach(course::addUser);
        course.addUser(owner);

        courseRepository.save(course);
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
    public void removeUserFromCourse(String courseCode, String userNumber) {
        Optional<Course> course=courseRepository.findByCode(courseCode);
        if (course.isEmpty()){
            throw new RuntimeException("There is no course with given code!");
        }
        List<User>users=course.get().getUsers();
        User user=userService.findByNumber(userNumber);
        users.remove(user);
        course.get().setUsers(users);

        courseRepository.save(course.get());
        log.info("user has been removed successfully from course");
    }


    private List<User> saveStudentsByFile(MultipartFile file) throws IOException {
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
                    // if it's not exists create new user
                    else {
                        student = new User(number, name, surname, number, "student");
                        userService.register(student);
                    }
                    students.add(student);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        log.info("course has been saved!");

        return students;
    }

    private CourseResponse courseToResponse(Course course) {

        return CourseResponse.builder().id(course.getId()).code(course.getCode()).owner(course.getOwner())
                .title(course.getTitle()).build();

    }
}