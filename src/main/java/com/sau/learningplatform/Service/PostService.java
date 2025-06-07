package com.sau.learningplatform.Service;


import com.sau.learningplatform.EntityResponse.PostResponse;

import java.util.List;

public interface PostService {
    List<PostResponse> getParentPostResponsesByProjectId(int id);

    void deleteById(int id);

}
