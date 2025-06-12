package com.sau.learningplatform.EntityResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPageResponse {
    List<UserResponse>users;

    private int pageSize;

    private int pageNo;

    private int totalPages;

}
