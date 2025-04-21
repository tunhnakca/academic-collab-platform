package com.sau.learningplatform.Repository;

import com.sau.learningplatform.Entity.Semester;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SemesterRepository extends JpaRepository<Semester,Integer> {
    List<Semester> findByIsActiveTrue();
}
