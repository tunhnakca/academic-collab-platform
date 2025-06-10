package com.sau.learningplatform.Repository;

import com.sau.learningplatform.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Integer> {
    List<Post> findByProjectIdAndParentPostIsNullOrderByDateCreatedAsc(int id);
}
