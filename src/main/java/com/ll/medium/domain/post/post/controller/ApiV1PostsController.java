package com.ll.medium.domain.post.post.controller;

import com.ll.medium.domain.post.post.dto.PostDto;
import com.ll.medium.domain.post.post.dto.PostForm;
import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.service.PostService;
import com.ll.medium.global.exception.ResourceNotFoundException;
import com.ll.medium.global.rq.Rq;
import com.ll.medium.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class ApiV1PostsController {
    private final PostService postService;
    private final Rq rq;

    @GetMapping("")
    public RsData<?> getPosts(@RequestParam(value="page", defaultValue="0") int page) {
        Page<Post> postEntities = postService.findByIsPublishedOrderByIdDesc(true, page);
        List<PostDto> postDtos = postEntities.stream().map(PostDto::new).toList();

        return RsData.of(
                "200",
                "전체 글 목록 조회 성공",
                postDtos
        );
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myList")
    public RsData<?> getMyPosts(@RequestParam(value="page", defaultValue="0") int page) {
        Page<Post> postEntities = postService.findByAuthorIdOrderByIdDesc(rq.getMember().getId(), page);
        List<PostDto> postDtos = postEntities.stream().map(PostDto::new).toList();

        return RsData.of(
                "200",
                "나의 글 목록 조회 성공",
                postDtos
        );
    }

    @GetMapping("/{id}")
    public RsData<?> getPost(@PathVariable long id) {
        Post post = postService.findById(id).orElseThrow(() -> new ResourceNotFoundException("해당 게시물을 찾을 수 없습니다."));
        if (!postService.canAccess(rq.getMember(), post)) throw new AccessDeniedException("조회권한이 없습니다.");

        PostDto postDto = new PostDto(post);

        return RsData.of(
                "200",
                "%d번 게시글이 조회되었습니다.".formatted(postDto.getId()),
                postDto
        );
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public RsData<?> writePost(@Valid @RequestBody PostForm postForm) {
        Post post = postService.write(rq.getMember(), postForm.getTitle(), postForm.getBody(), postForm.isPublished());
        PostDto postDto = new PostDto(post);

        return RsData.of(
                "200",
                "%d번 게시글이 작성되었습니다.".formatted(postDto.getId()),
                postDto
        );
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public RsData<?> modifyPost(
            @PathVariable long id,
            @Valid @RequestBody PostForm postForm
    ) {
        Post post = postService.findById(id).orElseThrow(() -> new ResourceNotFoundException("해당 게시물을 찾을 수 없습니다."));
        if (!postService.canModify(rq.getMember(), post)) throw new AccessDeniedException("수정권한이 없습니다.");

        postService.modify(post, postForm.getTitle(), postForm.getBody(), postForm.isPublished());
        PostDto postDto = new PostDto(post);

        return RsData.of(
                "200",
                "%d번 게시글이 수정되었습니다.".formatted(postDto.getId()),
                postDto
        );
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public RsData<?> deletePost(
            @PathVariable long id
    ) {
        Post post = postService.findById(id).orElseThrow(() -> new ResourceNotFoundException("해당 게시물을 찾을 수 없습니다."));
        if (!postService.canDelete(rq.getMember(), post)) throw new AccessDeniedException("삭제권한이 없습니다.");

        postService.delete(post);
        PostDto postDto = new PostDto(post);

        return RsData.of(
                "200",
                "%d번 게시글이 삭제되었습니다.".formatted(postDto.getId()),
                postDto
        );
    }
}
