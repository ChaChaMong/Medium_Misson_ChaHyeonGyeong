package com.ll.medium.domain.article.article.repository;

import com.ll.medium.domain.article.article.entity.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findAllByOrderByIdDesc(Pageable pageable);
    List<Article> findByIsPublishedOrderByIdDesc(boolean isPublished);
    //List<Article> findByIdByOrderByIdDesc(long id);
    List<Article> findByAuthorIdOrderByIdDesc(Long authorId);
}