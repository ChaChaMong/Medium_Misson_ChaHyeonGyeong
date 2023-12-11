package com.ll.medium.domain.home.home.controller;

import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.service.PostService;
import com.ll.medium.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final Rq rq;
    private final PostService postService;

    @GetMapping("/")
    public String showMain(Model model) {
        List<Post> posts = postService.findTop30ByIsPublishedOrderByIdDesc(true);
        //rq.setAttribute("posts", postService.findTop30ByIsPublishedOrderByIdDesc(true));
        model.addAttribute("posts", posts);
        model.addAttribute("detailUrl", "/post");

        return "domain/home/home/main";
    }
}
