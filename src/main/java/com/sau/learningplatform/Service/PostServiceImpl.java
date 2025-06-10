package com.sau.learningplatform.Service;

import com.sau.learningplatform.Entity.Post;
import com.sau.learningplatform.Entity.Project;
import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.EntityResponse.*;
import com.sau.learningplatform.Repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PostServiceImpl implements PostService{
    private PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    @Override
    public List<PostResponse> getParentPostResponsesByProjectId(int id) {
        List<Post>posts=postRepository.findByProjectIdAndParentPostIsNullOrderByDateCreatedAsc(id);
        if (posts.isEmpty()){
            log.info("No posts found for the given project!");
        }
        return posts.stream().map(this::postToResponse).toList();
    }

    @Override
    public void deleteById(int id) {
        postRepository.deleteById(id);
    }


    @Override
    public Post findById(int parentPostId) {
        Optional<Post>post=postRepository.findById(parentPostId);

        if (post.isEmpty()){
            log.warn("There is no post with given id!");
            throw new RuntimeException("There is no post with given id!");
        }

        return post.get();
    }

    @Override
    public MessageResponseWithStatus savePostRequest(User user, Project project, PostRequest request) {

        Post post = new Post();
        post.setUser(user);
        post.setProject(project);
        post.setText(convertMarkdownToHtml(request.getText()));
        post.setRepliedToNumber(request.getRepliedToNumber());

        if (request.getParentPostId() != null) {
            Optional<Post> parent = postRepository.findById(request.getParentPostId().intValue());

            if (parent.isEmpty()){
                log.warn("There is no post with given id!");
                return new MessageResponseWithStatus("error! Your comment has not been saved",false);
            }
            post.setParentPost(parent.get());
            parent.get().addReply(post);
            postRepository.save(parent.get());
        }
        else {
            postRepository.save(post);
        }

        return new MessageResponseWithStatus("Your comment has been saved successfully",true);
    }


    private PostResponse postToResponse(Post post){

        User user=post.getUser();
        List<ReplyResponse>replies=new ArrayList<>();

        if(!post.getReplies().isEmpty()){
            replies=post.getReplies().stream().sorted(Comparator.comparing(Post::getDateCreated)).map(this::postToReplyResponse).toList();
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

    private String convertMarkdownToHtml(String markdown) {
        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(parser.parse(markdown));
    }

}
