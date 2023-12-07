package com.ll.medium.domain.article.article.controller;

import com.ll.medium.domain.article.article.entity.Article;
import com.ll.medium.domain.article.article.service.ArticleService;
import com.ll.medium.global.rq.Rq;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final Rq rq;

    @GetMapping("/post/latest")
    String showLatestPosts(Model model) {
        List<Article> articles = articleService.findAllByOrderByIdDesc(30);
        model.addAttribute("articles", articles);

        return "domain/home/home/main";
    }

    @GetMapping("/post/list")
    String showList(Model model) {
        List<Article> articles = articleService.findByIsPublishedOrderByIdDesc(true);
        model.addAttribute("articles", articles);

        return "domain/article/article/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/post/myList")
    String showMyList(Model model) {
        List<Article> articles = articleService.findByAuthorIdOrderByIdDesc(rq.getMember().getId());
        model.addAttribute("articles", articles);

        return "domain/article/article/myList";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/post/write")
    String showWrite() {
        return "domain/article/article/write";
    }

    @Data
    public static class WriteForm {
        @NotBlank
        private String title;
        @NotBlank
        private String body;
        private String isPublished;

        private boolean getIsPublished() {
            return "true".equals(isPublished);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/post/write")
    String write(@Valid WriteForm writeForm){
        Article article = articleService.write(rq.getMember(), writeForm.title, writeForm.body, writeForm.getIsPublished());

        return rq.redirect("/", "%d번 게시물 생성되었습니다.".formatted(article.getId()));
    }
}
