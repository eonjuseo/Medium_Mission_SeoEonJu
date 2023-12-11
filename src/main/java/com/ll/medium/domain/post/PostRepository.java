package com.ll.medium.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ll.medium.domain.post.Post;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findById(Long id);


}
