package com.sau.learningplatform.Repository;

import com.sau.learningplatform.Entity.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SemesterRepository extends JpaRepository<Semester,Integer> {
    @Query("SELECT s FROM Semester s WHERE s.startDate <= :now AND s.endDate >= :now ORDER BY s.startDate ASC")
    List<Semester> findActiveSemesters(@Param("now") LocalDateTime now);

}
