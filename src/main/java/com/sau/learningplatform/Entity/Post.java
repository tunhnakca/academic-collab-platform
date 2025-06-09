package com.sau.learningplatform.Entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@SQLDelete(sql = "UPDATE posts SET is_deleted=true WHERE id = ?")
@Where(clause = "is_deleted = false")

public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "parent_post_id")
    private Post parentPost;

    @OneToMany(mappedBy = "parentPost", cascade = CascadeType.ALL)
    private List<Post> replies = new ArrayList<>();

    @Column(name = "replied_to_number")
    private String RepliedToNumber;
    @Column(name = "text")
    private String text;

    @Column(name = "date_created")
    @CreationTimestamp
    private LocalDateTime dateCreated;
    @Column(name = "is_deleted")
    private Boolean isDeleted = Boolean.FALSE;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Post getParentPost() {
        return parentPost;
    }

    public void setParentPost(Post parentPost) {
        this.parentPost = parentPost;
    }

    public List<Post> getReplies() {
        return replies;
    }

    public void setReplies(List<Post> replies) {
        this.replies = replies;
    }

    public String getRepliedToNumber() {
        return RepliedToNumber;
    }

    public void setRepliedToNumber(String repliedToNumber) {
        RepliedToNumber = repliedToNumber;
    }

    public void addReply(Post reply){

        if (replies.isEmpty()){
            replies=new ArrayList<>();
        }
        replies.add(reply);
    }
}