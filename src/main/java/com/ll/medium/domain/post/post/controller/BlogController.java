package com.ll.medium.domain.post.post.controller;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BlogController {
    private final PostService postService;
    private final MemberService memberService;

    @GetMapping("/b/{username}")
    public String showListByUsername(Model model, @PathVariable String username) {
        Member member = memberService.findByUsername(username).get();
        List<Post> posts = postService.findByAuthorIdOrderByIdDesc(member.getId());
        model.addAttribute("username", username);
        model.addAttribute("posts", posts);

        return "domain/post/post/blogList";
    }

}
