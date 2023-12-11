package com.ll.medium.domain.post.post.controller;

import com.ll.medium.domain.post.post.dto.PostForm;
import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.service.PostService;
import com.ll.medium.global.rq.Rq;
import jakarta.validation.Valid;
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

    @GetMapping("/post/list")
    public String showList(Model model) {
        List<Post> posts = postService.findByIsPublishedOrderByIdDesc(true);
        model.addAttribute("posts", posts);
        model.addAttribute("detailUrl", "/post");

        return "domain/post/post/postList";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/post/myList")
    public String showMyList(Model model) {
        List<Post> posts = postService.findByAuthorIdOrderByIdDesc(rq.getMember().getId());
        model.addAttribute("posts", posts);
        model.addAttribute("mappingUrl", "'/post/${post.id}'");

        return "domain/post/post/myList";
    }

    @GetMapping("/post/{id}")
    public String showDetail(Model model, @PathVariable long id) {
        Post post = postService.findById(id).get();

        model.addAttribute("post", post);

        return "domain/post/post/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/post/write")
    public String showWrite() {
        return "domain/post/post/write";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/post/write")
    public String write(@Valid PostForm postForm){
        Post post = postService.write(rq.getMember(), postForm.getTitle(), postForm.getBody(), postForm.isPublished());

        return rq.redirect("/post/%d".formatted(post.getId()), "%d번 게시물 생성되었습니다.".formatted(post.getId()));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/post/{id}/modify")
    public String showModify(Model model, @PathVariable long id){
        Post post = postService.findById(id).get();

        if (!postService.canModify(rq.getMember(), post)) throw new RuntimeException("수정권한이 없습니다.");

        model.addAttribute("post", post);

        return "domain/post/post/modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/post/{id}/modify")
    public String modify(@PathVariable long id, @Valid PostForm postForm){
        Post post = postService.findById(id).get();

        if (!postService.canModify(rq.getMember(), post)) throw new RuntimeException("수정권한이 없습니다.");

        postService.modify(post, postForm.getTitle(), postForm.getBody(), postForm.isPublished());

        return rq.redirect("/post/%d".formatted(post.getId()), "%d번 게시물 수정되었습니다.".formatted(id));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/post/{id}/delete")
    public String delete(@PathVariable long id) {
        Post post = postService.findById(id).get();

        if (!postService.canDelete(rq.getMember(), post)) throw new RuntimeException("삭제권한이 없습니다.");

        postService.delete(post);

        return rq.redirect("/post/myList", "%d번 게시물 삭제되었습니다.".formatted(id));
    }
}
