package com.sau.learningplatform.Repository;

import com.sau.learningplatform.Entity.Course;
import com.sau.learningplatform.Entity.Project;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Integer> {
    List<Project>findByCourseId(int id);

    List<Project> findByCourseCodeAndTitleContainingIgnoreCase(String CourseCode,String title);

    //non-expired date
    List<Project> findByDateEndAfter(LocalDateTime date);
    //expired date
    List<Project> findByDateEndBefore(LocalDateTime date);

    //oldest to newest
    List<Project> findByOrderByDateCreatedAsc();

    //newest to oldest
    List<Project> findByOrderByDateCreatedDesc();
}
