package com.sau.learningplatform.EntityResponse;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
public class PostResponse {
    private String nameAndSurname;

    private String text;

    private LocalDateTime dateCreated;

}
