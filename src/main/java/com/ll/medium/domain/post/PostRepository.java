package com.ll.medium.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Post findBySubject(String subject);
    Optional<Post> findById(Long id);
    Post findBySubjectAndContent(String subject, String content);
    List<Post> findBySubjectLike(String subject);

}
