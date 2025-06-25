package com.sau.learningplatform.Repository;

import com.sau.learningplatform.Entity.Course;
import com.sau.learningplatform.Entity.CourseRegistration;
import com.sau.learningplatform.Entity.Semester;
import com.sau.learningplatform.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRegistrationRepository extends JpaRepository<CourseRegistration,Integer> {

    Optional<CourseRegistration> findByCourseIdAndUserId(int courseId, int userId);

    List<CourseRegistration> findByUserIdAndSemesterId(int userId, Long semesterId);

    @Query("SELECT cr FROM CourseRegistration cr " +
            "WHERE cr.course.code = :courseCode " +
            "AND cr.user.role = :role " +
            "AND cr.semester = :semester " +
            "ORDER BY LOWER(cr.user.name) ASC")
    Page<CourseRegistration> findSortedUsersByNameIgnoreCase(
            @Param("courseCode") String courseCode,
            @Param("role") String role,
            @Param("semester") Semester semester,
            Pageable pageable);
    List<CourseRegistration> findByUserRoleIgnoreCaseAndSemester(String userRole, Semester semester);

    boolean existsByUserAndCourseAndSemester(User user, Course course, Semester semester);

}
