package com.sau.learningplatform.Entity;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
@SQLDelete(sql = "UPDATE courses SET is_deleted=true WHERE id = ?")
@Where(clause = "is_deleted = false")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.DETACH})
    @JoinTable(name="course_users",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users = new ArrayList<>();

    @Column(name = "title")
    private String title;
    @Column(name = "owner")
    private String owner;
    @Column(name = "code")
    private String code;

    @Column(name = "is_deleted")
    private Boolean isDeleted=Boolean.FALSE;

    public Course() {
    }
    public Course(String title, String owner, String code) {
        this.title = title;
        this.owner = owner;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public void addUser(User user){
        users.add(user);
    }

}
