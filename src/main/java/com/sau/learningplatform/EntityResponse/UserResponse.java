package com.sau.learningplatform.EntityResponse;

import com.sau.learningplatform.Entity.Course;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class UserResponse {
    private int id;

    private String number;

    private String name;

    private String surname;

    private String role;

}
