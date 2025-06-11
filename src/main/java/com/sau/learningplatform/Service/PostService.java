package com.sau.learningplatform.Service;


import com.sau.learningplatform.Entity.Post;
import com.sau.learningplatform.Entity.Project;
import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.EntityResponse.*;
import org.springframework.data.domain.Page;

public interface PostService {

    void deleteById(int id);


    Post findById(int parentPostId);

    MessageResponseWithStatus savePostRequest(User user, Project project, PostRequest request);

    PostPageResponse postsPagesToPageResponse(int pageNo, int pageSize, Page<Post>PostPages);

    PostPageResponse getParentPostsAsPostPageResponseByProjectId(int projectId, int pageNo, int pageSize);

    PostPageResponse searchParentPostsAsPostPageResponseByProjectId(String keyword, int projectId, int pageNo, int pageSize);
}
