package com.sau.learningplatform.Repository;

import com.sau.learningplatform.Entity.Course;
import com.sau.learningplatform.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course,Integer> {

    Optional<Course> findByCode(String code);

    boolean existsByCode(String code);


}
