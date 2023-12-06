package com.ll.medium.domain.article.article.controller;

import com.ll.medium.domain.article.article.entity.Article;
import com.ll.medium.domain.article.article.service.ArticleService;
import com.ll.medium.global.rq.Rq;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/article/list")
    String showList(Model model) {
        List<Article> articles = articleService.findAll();
        model.addAttribute("articles", articles);

        return "domain/article/article/list";
    }

    @GetMapping("/article/write")
    String showWrite() {
        return "domain/article/article/write";
    }

    @Data
    public static class WriteForm {
        @NotBlank
        private String title;
        @NotBlank
        private String body;
    }

    @PostMapping("/article/write")
    String write(@Valid WriteForm writeForm){
        Article article = articleService.write(rq.getMember(), writeForm.title, writeForm.body);

        return rq.redirect("/", "%d번 게시물 생성되었습니다.".formatted(article.getId()));
    }
}
