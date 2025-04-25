package com.sau.learningplatform.Repository;

import com.sau.learningplatform.Entity.CourseRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRegistrationRepository extends JpaRepository<CourseRegistration,Integer> {

    Optional<CourseRegistration> findByCourseIdAndUserId(int courseId, int userId);

    List<CourseRegistration> findByUserIdAndSemesterId(int userId, Long semesterId);

    List<CourseRegistration> findByCourseCodeAndUserRole(String courseCode, String userRole);

}
