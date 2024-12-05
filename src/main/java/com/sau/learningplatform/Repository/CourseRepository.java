package com.sau.learningplatform.Repository;

import com.sau.learningplatform.Entity.Course;
import com.sau.learningplatform.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course,Integer> {
    List<Course>findCoursesByUsers(User user);
}
