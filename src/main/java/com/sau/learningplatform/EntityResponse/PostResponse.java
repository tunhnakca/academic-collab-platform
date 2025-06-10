package com.sau.learningplatform.EntityResponse;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
public class PostResponse {

    private int id;

    private String name;

    private String surname;

    private String number;

    private String userRole;

    private List<ReplyResponse> replies;

    private String text;

    private LocalDateTime dateCreated;

}
