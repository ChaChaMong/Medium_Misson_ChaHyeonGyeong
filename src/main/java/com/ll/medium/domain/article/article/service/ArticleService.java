package com.ll.medium.domain.article.article.service;

import com.ll.medium.domain.article.article.entity.Article;
import com.ll.medium.domain.article.article.repository.ArticleRepository;
import com.ll.medium.domain.member.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public List<Article> findLatestPosts(int count) {
        PageRequest pageRequest = PageRequest.of(0, count, Sort.by(Sort.Direction.DESC, "id"));

        return articleRepository.findAllByOrderByIdDesc(pageRequest);
    }

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

}
