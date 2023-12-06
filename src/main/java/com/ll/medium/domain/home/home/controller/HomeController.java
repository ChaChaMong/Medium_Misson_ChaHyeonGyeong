package com.ll.medium.domain.home.home.controller;

import com.ll.medium.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final Rq rq;

    @GetMapping("/")
    public String goToLatestPosts(String msg) {
        return rq.redirect("/post/latest", msg);
    }
}
