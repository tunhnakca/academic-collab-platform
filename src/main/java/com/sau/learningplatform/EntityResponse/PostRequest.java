package com.sau.learningplatform.EntityResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    private int projectId;
    private Long parentPostId; // it could be null
    private String repliedToNumber;
    private String text;

}
