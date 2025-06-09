package com.sau.learningplatform.Service;

import com.sau.learningplatform.Entity.Post;
import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.EntityResponse.PostResponse;
import com.sau.learningplatform.EntityResponse.ReplyResponse;
import com.sau.learningplatform.Repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PostServiceImpl implements PostService{
    private PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    @Override
    public List<PostResponse> getParentPostResponsesByProjectId(int id) {
        List<Post>posts=postRepository.findByProjectIdAndParentPostIsNull(id);
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

        User user=post.getUser();
        List<ReplyResponse>replies=new ArrayList<>();

        if(!post.getReplies().isEmpty()){
            replies=post.getReplies().stream().map(this::postToReplyResponse).toList();
        }

        return PostResponse.builder()
                .text(post.getText())
                .name(user.getName())
                .surname(user.getSurname())
                .number(user.getNumber())
                .replies(replies)
                .dateCreated(post.getDateCreated())
                .build();
    }


    private ReplyResponse postToReplyResponse(Post post){

        User user=post.getUser();

        return ReplyResponse.builder()
                .text(post.getText())
                .name(user.getName())
                .surname(user.getSurname())
                .number(user.getNumber())
                .repliedToNumber(post.getRepliedToNumber())
                .dateCreated(post.getDateCreated())
                .parentPostId(post.getParentPost().getId())
                .build();
    }

}
