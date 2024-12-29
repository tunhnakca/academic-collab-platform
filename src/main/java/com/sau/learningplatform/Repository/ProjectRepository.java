package com.sau.learningplatform.Repository;

import com.sau.learningplatform.Entity.Course;
import com.sau.learningplatform.Entity.Project;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Integer> {
    List<Project>findByCourseId(int id);

    List<Project> findByCourseCodeAndTitleContainingIgnoreCase(String CourseCode,String title);

    //non-expired date
    List<Project> findByCourseCodeAndDateEndAfter(String CourseCode,LocalDateTime date);
    //expired date
    List<Project> findByCourseCodeAndDateEndBefore(String CourseCode,LocalDateTime date);

    //oldest to newest
    List<Project> findByCourseCodeOrderByDateCreatedAsc(String CourseCode);

    //newest to oldest
    List<Project> findByCourseCodeOrderByDateCreatedDesc(String CourseCode);

}
