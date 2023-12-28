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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/posts", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "ApiV1PostController", description = "글 CRUD 컨트롤러")
@SecurityRequirement(name = "bearerAuth")
public class ApiV1PostsController {
    private final PostService postService;
    private final Rq rq;

    @GetMapping(value = "latest", consumes = ALL_VALUE)
    @SecurityRequirement(name = "none")
    @Operation(summary = "최신 글 리스트")
    public RsData<List<PostDto>> getLatestPosts() {
        List<Post> postEntities = postService.findTop30ByIsPublishedOrderByIdDesc(true);
        List<PostDto> postDtos = postEntities.stream().map(PostDto::new).toList();

        return RsData.of(
                "200",
                SuccessMessage.GET_LATEST_POSTS_SUCCESS.getMessage(),
                postDtos
        );
    }

    @GetMapping(value = "list", consumes = ALL_VALUE)
    @SecurityRequirement(name = "none")
    @Operation(summary = "글 리스트")
    public RsData<List<PostDto>> getPosts(@RequestParam(value="page", defaultValue="0") int page) {
        Page<Post> postEntities = postService.findByIsPublishedOrderByIdDesc(true, page);
        List<PostDto> postDtos = postEntities.stream().map(PostDto::new).toList();

        return RsData.of(
                "200",
                SuccessMessage.GET_POSTS_SUCCESS.getMessage(),
                postDtos
        );
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/myList", consumes = ALL_VALUE)
    @Operation(summary = "내 글 리스트")
    public RsData<List<PostDto>> getMyPosts(@RequestParam(value="page", defaultValue="0") int page) {
        Page<Post> postEntities = postService.findByAuthorIdOrderByIdDesc(rq.getMember().getId(), page);
        List<PostDto> postDtos = postEntities.stream().map(PostDto::new).toList();

        return RsData.of(
                "200",
                SuccessMessage.GET_MY_POSTS_SUCCESS.getMessage(),
                postDtos
        );
    }

    @GetMapping(value = "/{id}", consumes = ALL_VALUE)
    @Operation(summary = "글 상세 조회")
    public RsData<PostDto> getPost(@PathVariable long id) {
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
    @PostMapping(value = "")
    @Operation(summary = "글 작성")
    public RsData<PostDto> writePost(@Valid @RequestBody PostRequestDto postRequestDto) {
        Post post = postService.write(rq.getMember(), postRequestDto.getTitle(), postRequestDto.getBody(), postRequestDto.isPublished());
        PostDto postDto = new PostDto(post);

        return RsData.of(
                "200",
                SuccessMessage.WRITE_POST_SUCCESS.getMessage().formatted(postDto.getId()),
                postDto
        );
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping(value = "/{id}")
    @Operation(summary = "글 수정")
    public RsData<PostDto> modifyPost(
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
    @DeleteMapping(value = "/{id}", consumes = ALL_VALUE)
    @Operation(summary = "글 삭제")
    public RsData<PostDto> deletePost(@PathVariable long id) {
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
