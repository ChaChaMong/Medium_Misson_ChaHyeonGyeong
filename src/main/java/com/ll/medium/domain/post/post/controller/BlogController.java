package com.ll.medium.domain.post.post.controller;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.service.PostService;
import com.ll.medium.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class BlogController {
    private final PostService postService;
    private final MemberService memberService;
    private final Rq rq;

    @GetMapping("/b/{username}")
    public String showListByUsername(Model model, @PathVariable String username, @RequestParam(value="page", defaultValue="0") int page) {
        Member member = memberService.findByUsername(username).get();
        Page<Post> posts = postService.findByAuthorIdOrderByIdDesc(member.getId(), page);
        model.addAttribute("username", username);
        model.addAttribute("paging", posts);
        model.addAttribute("detailUrl", "/b/%s".formatted(username));

        return "domain/post/post/blogList";
    }

    @GetMapping("/b/{username}/{id}")
    public String showDetail(Model model, @PathVariable long id) {
        Post post = postService.findById(id).get();

        model.addAttribute("post", post);

        if (!postService.canAccess(rq.getMember(), post)) throw new RuntimeException("조회권한이 없습니다.");

        return "domain/post/post/detail";
    }
}
