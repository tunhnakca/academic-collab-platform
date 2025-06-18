package com.sau.learningplatform.Service;

import com.sau.learningplatform.Entity.Post;
import com.sau.learningplatform.Entity.Project;
import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.EntityResponse.*;
import com.sau.learningplatform.Repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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



    @Override
    public PostPageResponse postsPagesToPageResponse(int pageNo, int pageSize, Page<Post> postsPages) {

        List<Post> posts=postsPages.getContent();
        if (posts.isEmpty()){
            log.info("Posts are empty.");
        }
        List<PostResponse>postResponses=posts.stream().map(this::postToResponse).toList();

        return PostPageResponse.builder()
                    .pageSize(pageSize)
                    .PageNo(pageNo)
                    .totalPages(postsPages.getTotalPages())
                    .posts(postResponses)
                    .build();
    }

    @Override
    public PostPageResponse getParentPostsAsPostPageResponseByProjectId(int projectId, int pageNo, int pageSize) {
        Pageable pageable= PageRequest.of(pageNo,pageSize);
        Page<Post>postPages=postRepository.findByProjectIdAndParentPostIsNullOrderByDateCreatedAsc(projectId,pageable);
        return postsPagesToPageResponse(pageNo,pageSize,postPages);
    }

    @Override
    public PostPageResponse searchParentPostsAsPostPageResponseByProjectId(String keyword, int projectId, int pageNo, int pageSize) {
        Pageable pageable=PageRequest.of(pageNo,pageSize);
        Page<Post>postPages=postRepository.searchPosts(keyword,projectId,pageable);
        return postsPagesToPageResponse(pageNo,pageSize,postPages);
    }

    @Override
    public MessageResponseWithStatus deletePost(User loggedUser, int postId) {
        Optional<Post>postOptional=postRepository.findById(postId);
        if(postOptional.isEmpty()){
            log.error("invalid postId to delete");
            return new MessageResponseWithStatus("There was an error while deleting your comment.",false);
        }
        if (postOptional.get().getUser().getId()!=loggedUser.getId()){
            if(!loggedUser.getRole().equalsIgnoreCase("admin")){
                return new MessageResponseWithStatus("You do not have permission to delete comments you do not own.",false);
            }
        }
        postRepository.deleteById(postId);
        log.info("The post has been deleted");
        return new MessageResponseWithStatus("Your post has been deleted successfully",true);
    }


    private PostResponse postToResponse(Post post){

        User user=post.getUser();
        List<ReplyResponse>replies=new ArrayList<>();

        if(!post.getReplies().isEmpty()){
            replies=post.getReplies().stream().sorted(Comparator.comparing(Post::getDateCreated)).map(this::postToReplyResponse).toList();
        }

        return PostResponse.builder()
                .id(post.getId())
                .text(post.getText())
                .name(user.getName())
                .surname(user.getSurname())
                .number(user.getNumber())
                .userRole(user.getRole().toLowerCase())
                .replies(replies)
                .dateCreated(post.getDateCreated())
                .build();
    }


    private ReplyResponse postToReplyResponse(Post post){

        User user=post.getUser();

        return ReplyResponse.builder()
                .id(post.getId())
                .text(post.getText())
                .name(user.getName())
                .surname(user.getSurname())
                .number(user.getNumber())
                .userRole(user.getRole().toLowerCase())
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
