package com.sau.learningplatform.EntityResponse;

import com.sau.learningplatform.Entity.Course;
import jakarta.persistence.Column;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;


@Data
public class ProjectResponse {

    private int id;

    private Course course;

    private String title;

    private Date dateCreated;

    private Date dateEnd;

    private String description;
}
