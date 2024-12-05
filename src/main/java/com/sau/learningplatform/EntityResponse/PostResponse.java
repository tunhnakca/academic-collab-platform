package com.sau.learningplatform.EntityResponse;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class PostResponse {
    private String username;

    private String text;

    private Date dateCreated;

}
