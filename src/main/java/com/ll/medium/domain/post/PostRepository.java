package com.ll.medium.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ll.medium.domain.post.Post;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findById(Long id);

    @Query("SELECT p FROM Post p WHERE p.createDate IS NOT NULL ORDER BY p.createDate DESC")
    List<Post> findTop30ByIsPublishedOrderByIdDesc();

    List<Post> findByCreatedBy(String username);


}
