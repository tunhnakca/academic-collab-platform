package com.sau.learningplatform.Repository;

import com.sau.learningplatform.Entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Integer> {
    List<Project>findByCourseId(int id);

    //project search
    @Query("SELECT p FROM Project p WHERE p.course.code = :courseCode " +
            "AND (LOWER(p.title) LIKE LOWER(CONCAT(:title, '%')) " +
            "OR LOWER(p.title) LIKE LOWER(CONCAT('% ', :title, '%')))")
    List<Project> searchByCourseCodeAndTitlePrefix(@Param("courseCode") String courseCode,
                                                   @Param("title") String title);
    
    //non-expired date
    List<Project> findByCourseCodeAndDateEndAfter(String CourseCode,LocalDateTime date);
    //expired date
    List<Project> findByCourseCodeAndDateEndBefore(String CourseCode,LocalDateTime date);

    //oldest to newest
    List<Project> findByCourseCodeOrderByDateCreatedAsc(String CourseCode);

    //newest to oldest
    List<Project> findByCourseCodeOrderByDateCreatedDesc(String CourseCode);

}
