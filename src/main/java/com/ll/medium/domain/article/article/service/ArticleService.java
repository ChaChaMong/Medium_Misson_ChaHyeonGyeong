package com.ll.medium.domain.article.article.service;

import com.ll.medium.domain.article.article.entity.Article;
import com.ll.medium.domain.article.article.repository.ArticleRepository;
import com.ll.medium.domain.member.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {
    private final ArticleRepository articleRepository;

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public List<Article> findAllByOrderByIdDesc(int count) {
        Pageable pageable = PageRequest.of(0, count);

        return articleRepository.findAllByOrderByIdDesc(pageable);
    }

    public List<Article> findByIsPublishedOrderByIdDesc(boolean isPublished) {
        return articleRepository.findByIsPublishedOrderByIdDesc(isPublished);
    }

    public List<Article> findByAuthorIdOrderByIdDesc(long authorId) {
        return articleRepository.findByAuthorIdOrderByIdDesc(authorId);
    }

    public Optional<Article> findById(long id) {
        return articleRepository.findById(id);
    }

    @Transactional
    public Article write(Member author, String title, String body, boolean isPublished) {
        Article article = Article.builder()
                .author(author)
                .title(title)
                .body(body)
                .isPublished(isPublished)
                .build();
        articleRepository.save(article);
        return article;
    }

    public boolean canDelete(Member actor, Article article) {
        if (actor == null) return false;

        if (actor.isAdmin()) return true;

        return article.getAuthor().equals(actor);
    }

    @Transactional
    public void delete(Article article) {
        articleRepository.delete(article);
    }

}
