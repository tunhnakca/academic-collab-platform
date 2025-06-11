package com.sau.learningplatform.EntityResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostPageResponse {
    List<PostResponse> posts;

    private int pageSize;

    private int PageNo;

    private int totalPages;
}
