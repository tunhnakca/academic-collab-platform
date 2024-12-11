package com.sau.learningplatform.EntityResponse;

import com.sau.learningplatform.Entity.Course;
import lombok.Builder;
import lombok.Data;




@Data
@Builder
public class ProjectResponse {

    private int id;

    private Course course;

    private String title;

    private boolean isValid;

    private String description;
}
