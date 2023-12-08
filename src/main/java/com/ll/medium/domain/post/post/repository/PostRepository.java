package com.ll.medium.domain.post.post.repository;

import com.ll.medium.domain.post.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByIdDesc(Pageable pageable);
    List<Post> findByIsPublishedOrderByIdDesc(boolean isPublished);
    //List<Post> findByIdByOrderByIdDesc(long id);
    List<Post> findByAuthorIdOrderByIdDesc(Long authorId);
}