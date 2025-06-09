package com.sau.learningplatform.EntityResponse;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
public class ProjectResponse {

    private int id;

    private CourseResponse course;

    private String title;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private boolean isActive;

    private String description;
}
