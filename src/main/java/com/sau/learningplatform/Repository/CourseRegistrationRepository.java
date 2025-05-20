package com.sau.learningplatform.Repository;

import com.sau.learningplatform.Entity.CourseRegistration;
import com.sau.learningplatform.Entity.Semester;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRegistrationRepository extends JpaRepository<CourseRegistration,Integer> {

    Optional<CourseRegistration> findByCourseIdAndUserId(int courseId, int userId);

    List<CourseRegistration> findByUserIdAndSemesterId(int userId, Long semesterId);

    Page<CourseRegistration> findByCourseCodeAndUserRoleAndSemester(String courseCode, String role, Semester semester, Pageable pageable);
    List<CourseRegistration> findByUserRoleIgnoreCaseAndSemester(String userRole, Semester semester);

}
