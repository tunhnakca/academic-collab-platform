package com.sau.learningplatform.Repository;

import com.sau.learningplatform.Entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Integer> {
    List<Project>findByCourseId(int id);
}
