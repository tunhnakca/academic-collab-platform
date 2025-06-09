package com.sau.learningplatform.Service;


import com.sau.learningplatform.Entity.Post;
import com.sau.learningplatform.Entity.Project;
import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.EntityResponse.MessageResponse;
import com.sau.learningplatform.EntityResponse.MessageResponseWithStatus;
import com.sau.learningplatform.EntityResponse.PostRequest;
import com.sau.learningplatform.EntityResponse.PostResponse;

import java.util.List;
import java.util.Optional;

public interface PostService {
    List<PostResponse> getParentPostResponsesByProjectId(int id);

    void deleteById(int id);


    Post findById(int parentPostId);

    MessageResponseWithStatus savePostRequest(User user, Project project, PostRequest request);
}
