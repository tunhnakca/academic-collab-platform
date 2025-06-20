package com.sau.learningplatform.Repository;

import com.sau.learningplatform.Entity.Project;
import com.sau.learningplatform.Entity.Semester;
import com.sau.learningplatform.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByNumber(String number);


    Optional<User> findByNumber(String number);

    List<User> findByRoleIgnoreCase(String role);

    //user search
    @Query("SELECT u FROM CourseRegistration cr " +
            "JOIN cr.user u " +
            "WHERE cr.course.code = :courseCode " +
            "AND cr.semester = :semester " +
            "AND cr.user.role = :role " +
            "AND (" +
            "LOWER(u.name) LIKE LOWER(CONCAT(:keyword, '%')) " +
            "OR LOWER(u.name) LIKE LOWER(CONCAT('% ', :keyword, '%')) " +
            "OR LOWER(u.surname) LIKE LOWER(CONCAT(:keyword, '%')) " +
            "OR LOWER(u.surname) LIKE LOWER(CONCAT('% ', :keyword, '%')))")
    Page<User> searchUsersByCourseAndRoleAndNameOrSurnamePaged(
            @Param("courseCode") String courseCode,
            @Param("semester") Semester semester,
            @Param("role") String role,
            @Param("keyword") String keyword,
            Pageable pageable);

    Optional<User> findByResetToken(String token);
}