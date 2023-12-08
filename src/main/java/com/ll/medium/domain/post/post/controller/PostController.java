package com.ll.medium.domain.post.post.controller;

import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.service.PostService;
import com.ll.medium.global.rq.Rq;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final Rq rq;

    @GetMapping("/post/latest")
    String showLatestPosts(Model model) {
        List<Post> posts = postService.findAllByOrderByIdDesc(30);
        model.addAttribute("posts", posts);

        return "domain/home/home/main";
    }

    @GetMapping("/post/list")
    String showList(Model model) {
        List<Post> posts = postService.findByIsPublishedOrderByIdDesc(true);
        model.addAttribute("posts", posts);

        return "domain/post/post/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/post/myList")
    String showMyList(Model model) {
        List<Post> posts = postService.findByAuthorIdOrderByIdDesc(rq.getMember().getId());
        model.addAttribute("posts", posts);

        return "domain/post/post/myList";
    }

    @GetMapping("/post/{id}")
    String showDetail(Model model, @PathVariable long id) {
        Post post = postService.findById(id).get();

        model.addAttribute("post", post);

        return "domain/post/post/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/post/write")
    String showWrite() {
        return "domain/post/post/write";
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
        Post post = postService.write(rq.getMember(), writeForm.title, writeForm.body, writeForm.getIsPublished());

        return rq.redirect("/", "%d번 게시물 생성되었습니다.".formatted(post.getId()));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/post/{id}/delete")
    String delete(@PathVariable long id) {
        Post post = postService.findById(id).get();

        if (!postService.canDelete(rq.getMember(), post)) throw new RuntimeException("삭제권한이 없습니다.");

        postService.delete(post);

        return rq.redirect("/", "%d번 게시물 삭제되었습니다.".formatted(id));
    }
}
