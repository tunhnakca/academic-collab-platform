package com.sau.learningplatform.Entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
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
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "project_id",nullable = false)
    private Project project;

    @OneToMany
    @JoinColumn(name = "child_post_id")
    private List<Post> childPost;


    @Column(name = "text")
    private String text;
    @Column(name = "status")
    private String status;
    @Column(name = "date_created")
    @CreationTimestamp

    private LocalDateTime dateCreated;
    @Column(name = "is_deleted")
    private Boolean isDeleted=Boolean.FALSE;

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

    public List<Post> getChildPost() {
        return childPost;
    }

    public void setChildPost(List<Post> childPost) {
        this.childPost = childPost;

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
