package com.sau.learningplatform.Repository;

import com.sau.learningplatform.Entity.Post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface PostRepository extends JpaRepository<Post,Integer> {
    Page<Post> findByProjectIdAndParentPostIsNullOrderByDateCreatedAsc(int id, Pageable pageable);
    @Query("""
    SELECT p FROM Post p
    WHERE p.project.id = :projectId
      AND p.parentPost IS NULL
      AND (
           LOWER(
               FUNCTION('regexp_replace',
                   FUNCTION('regexp_replace', p.text, '<[^>]*>', ' ', 'g'),
                   '&[a-zA-Z0-9#]+;', '', 'g')
           ) LIKE LOWER(CONCAT(:keyword, '%'))
           OR
           LOWER(
               FUNCTION('regexp_replace',
                   FUNCTION('regexp_replace', p.text, '<[^>]*>', ' ', 'g'),
                   '&[a-zA-Z0-9#]+;', '', 'g')
           ) LIKE LOWER(CONCAT('% ', :keyword, '%'))
      )
    ORDER BY p.dateCreated ASC
""")
    Page<Post> searchPosts(@Param("keyword") String keyword,
                                           @Param("projectId") int projectId,
                                           Pageable pageable);
}
