package com.sau.learningplatform.EntityResponse;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReplyResponse {

    private int parentPostId;

    private int id;
    
    private String name;

    private String surname;

    private String number;

    private String userRole;

    private String repliedToNumber;

    private String text;

    private LocalDateTime dateCreated;

}