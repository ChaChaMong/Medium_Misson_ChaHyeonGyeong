package com.ll.medium.domain.post.post.controller;

import com.ll.medium.domain.post.post.dto.PostDto;
import com.ll.medium.domain.post.post.dto.PostRequestDto;
import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.service.PostService;
import com.ll.medium.global.common.ErrorMessage;
import com.ll.medium.global.common.SuccessMessage;
import com.ll.medium.global.exception.CustomAccessDeniedException;
import com.ll.medium.global.exception.ResourceNotFoundException;
import com.ll.medium.global.rq.Rq;
import com.ll.medium.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class ApiV1PostsController {
    private final PostService postService;
    private final Rq rq;


    @GetMapping("latest")
    public RsData<?> getLatestPosts() {
        List<Post> postEntities = postService.findTop30ByIsPublishedOrderByIdDesc(true);
        List<PostDto> postDtos = postEntities.stream().map(PostDto::new).toList();

        return RsData.of(
                "200",
                SuccessMessage.GET_LATEST_POSTS_SUCCESS.getMessage(),
                postDtos
        );
    }

    @GetMapping("")
    public RsData<?> getPosts(@RequestParam(value="page", defaultValue="0") int page) {
        Page<Post> postEntities = postService.findByIsPublishedOrderByIdDesc(true, page);
        List<PostDto> postDtos = postEntities.stream().map(PostDto::new).toList();

        return RsData.of(
                "200",
                SuccessMessage.GET_POSTS_SUCCESS.getMessage(),
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
                SuccessMessage.GET_MY_POSTS_SUCCESS.getMessage(),
                postDtos
        );
    }

    @GetMapping("/{id}")
    public RsData<?> getPost(@PathVariable long id) {
        Post post = postService.findById(id).orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.POST_NOT_FOUND.getMessage()));
        if (!postService.canAccess(rq.getMember(), post)) throw new CustomAccessDeniedException(ErrorMessage.NO_ACCESS.getMessage());

        PostDto postDto = new PostDto(post);

        return RsData.of(
                "200",
                SuccessMessage.GET_POST_SUCCESS.getMessage().formatted(postDto.getId()),
                postDto
        );
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public RsData<?> writePost(@Valid @RequestBody PostRequestDto postRequestDto) {
        Post post = postService.write(rq.getMember(), postRequestDto.getTitle(), postRequestDto.getBody(), postRequestDto.isPublished());
        PostDto postDto = new PostDto(post);

        return RsData.of(
                "200",
                SuccessMessage.WRITE_POST_SUCCESS.getMessage().formatted(postDto.getId()),
                postDto
        );
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public RsData<?> modifyPost(
            @PathVariable long id,
            @Valid @RequestBody PostRequestDto postRequestDto
    ) {
        Post post = postService.findById(id).orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.POST_NOT_FOUND.getMessage()));
        if (!postService.canModify(rq.getMember(), post)) throw new CustomAccessDeniedException(ErrorMessage.NO_MODIFY_PERMISSION.getMessage());

        postService.modify(post, postRequestDto.getTitle(), postRequestDto.getBody(), postRequestDto.isPublished());
        PostDto postDto = new PostDto(post);

        return RsData.of(
                "200",
                SuccessMessage.MODIFY_POST_SUCCESS.getMessage().formatted(postDto.getId()),
                postDto
        );
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public RsData<?> deletePost(@PathVariable long id) {
        Post post = postService.findById(id).orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.POST_NOT_FOUND.getMessage()));
        if (!postService.canDelete(rq.getMember(), post)) throw new CustomAccessDeniedException(ErrorMessage.NO_DELETE_PERMISSION.getMessage());

        postService.delete(post);
        PostDto postDto = new PostDto(post);

        return RsData.of(
                "200",
                SuccessMessage.DELETE_POST_SUCCESS.getMessage().formatted(postDto.getId()),
                postDto
        );
    }
}
