package com.ll.medium.domain.article.article.service;

import com.ll.medium.domain.article.article.entity.Article;
import com.ll.medium.domain.article.article.repository.ArticleRepository;
import com.ll.medium.domain.member.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public Article write(Member author, String title, String body) {
        Article article = Article.builder()
                .author(author)
                .title(title)
                .body(body)
                .build();
        articleRepository.save(article);
        return article;
    }

}
