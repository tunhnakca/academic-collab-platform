package com.sau.learningplatform.EntityResponse;

import com.sau.learningplatform.Entity.Course;
import lombok.Data;

import java.util.List;


@Data
public class UserResponse {
    private int id;

    private String username;

    private String password;

    private List<Course> courses;

    private String number;

    private String name;

    private String surname;

    private String role;

}
