package com.sau.learningplatform.EntityResponse;

import com.sau.learningplatform.Entity.Course;
import lombok.Builder;
import lombok.Data;

import java.sql.Time;
import java.time.LocalDateTime;


@Data
@Builder
public class ProjectResponse {

    private int id;

    private CourseResponse course;

    private String title;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private boolean isValid;

    private String description;
}
