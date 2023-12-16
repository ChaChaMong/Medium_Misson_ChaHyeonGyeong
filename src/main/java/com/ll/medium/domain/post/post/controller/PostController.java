package com.ll.medium.domain.post.post.controller;

import com.ll.medium.domain.post.post.dto.PostForm;
import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.service.PostService;
import com.ll.medium.global.rq.Rq;
import com.ll.medium.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final Rq rq;

    @GetMapping("/list")
    public String showList(Model model, @RequestParam(value="page", defaultValue="0") int page) {
        Page<Post> paging = this.postService.findByIsPublishedOrderByIdDesc(true, page);
        model.addAttribute("paging", paging);
        model.addAttribute("detailUrl", "/post");

        return "domain/post/post/postList";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myList")
    public String showMyList(Model model, @RequestParam(value="page", defaultValue="0") int page) {
        Page<Post> posts = postService.findByAuthorIdOrderByIdDesc(rq.getMember().getId(), page);
        model.addAttribute("paging", posts);
        model.addAttribute("detailUrl", "/post");

        return "domain/post/post/myList";
    }

    @GetMapping("/{id}")
    public String showDetail(Model model, @PathVariable long id) {
        Post post = postService.findById(id).get();

        model.addAttribute("post", post);

        if (!postService.canAccess(rq.getMember(), post)) {
            return rq.historyBack("조회권한이 없습니다.");
        }

        return "domain/post/post/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String showWrite() {
        return "domain/post/post/write";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String write(@Valid PostForm postForm){
        Post post = postService.write(rq.getMember(), postForm.getTitle(), postForm.getBody(), postForm.isPublished());

        RsData<Post> writeRs = RsData.of("200", "%d번 게시글이 작성되었습니다.".formatted(post.getId()), post);

        return rq.redirect("/post/%d".formatted(writeRs.getData().getId()), writeRs.getMsg());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/modify")
    public String showModify(Model model, @PathVariable long id){
        Post post = postService.findById(id).get();

        if (!postService.canModify(rq.getMember(), post)) {
            return rq.historyBack("수정권한이 없습니다.");
        }

        model.addAttribute("post", post);

        return "domain/post/post/modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}/modify")
    public String modify(@PathVariable long id, @Valid PostForm postForm){
        Post post = postService.findById(id).get();

        if (!postService.canModify(rq.getMember(), post)) {
            return rq.historyBack("수정권한이 없습니다.");
        }

        postService.modify(post, postForm.getTitle(), postForm.getBody(), postForm.isPublished());

        RsData<Post> modifyRs = RsData.of("200", "%d번 게시글이 수정되었습니다.".formatted(post.getId()), post);

        return rq.redirect("/post/%d".formatted(modifyRs.getData().getId()), modifyRs.getMsg());
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable long id) {
        Post post = postService.findById(id).get();

        if (!postService.canDelete(rq.getMember(), post)) {
            return rq.historyBack("삭제권한이 없습니다.");
        }

        postService.delete(post);

        RsData<Post> deleteRs = RsData.of("200", "%d번 게시글이 삭제되었습니다.".formatted(post.getId()), post);

        return rq.redirect("/post/myList", deleteRs.getMsg());
    }
}
