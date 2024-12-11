package com.sau.learningplatform.Service;

import com.sau.learningplatform.Entity.Post;
import com.sau.learningplatform.EntityResponse.PostResponse;
import com.sau.learningplatform.Repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PostServiceImpl implements PostService{
    private PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    @Override
    public List<PostResponse> getPostResponsesByProjectId(int id) {
        List<Post>posts=postRepository.findByProjectId(id);
        if (posts.isEmpty()){
            log.info("No posts found for the given project!");
        }
        return posts.stream().map(this::postToResponse).toList();
    }

    @Override
    public void deleteById(int id) {
        postRepository.deleteById(id);
    }


    private PostResponse postToResponse(Post post){

        return PostResponse.builder().
                text(post.getText()).
                nameAndSurname(post.getUser().getName()+" "+post.getUser().getSurname()).
                dateCreated(post.getDateCreated()).
                build();
    }

}
