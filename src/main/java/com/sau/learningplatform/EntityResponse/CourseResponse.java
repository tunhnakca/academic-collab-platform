package com.sau.learningplatform.EntityResponse;

import com.sau.learningplatform.Entity.Project;
import com.sau.learningplatform.Entity.User;
import lombok.Builder;
import lombok.Data;



@Data
@Builder
public class CourseResponse {

    private int id;

    private String owner;

    private String title;

    private String code;

    private int studentCount;

    private int openProjectCount;

    private int closedProjectCount;

}
