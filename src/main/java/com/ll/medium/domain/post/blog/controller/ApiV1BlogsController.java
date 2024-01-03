package com.ll.medium.domain.post.blog.controller;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.domain.post.post.dto.PostDto;
import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.service.PostService;
import com.ll.medium.global.app.AppConfig;
import com.ll.medium.global.common.ErrorMessage;
import com.ll.medium.global.common.SuccessMessage;
import com.ll.medium.global.exception.CustomAccessDeniedException;
import com.ll.medium.global.exception.ResourceNotFoundException;
import com.ll.medium.global.rq.Rq;
import com.ll.medium.global.rsData.RsData;
import com.ll.medium.standard.base.PageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.*;

@RestController
@RequestMapping(value = "/api/v1/b", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "ApiV1BlogController", description = "특정 사용자의 글 관련 컨트롤러")
@SecurityRequirement(name = "bearerAuth")
public class ApiV1BlogsController {
    private final PostService postService;
    private final MemberService memberService;
    private final Rq rq;

    @GetMapping(value = "/{username}", consumes = ALL_VALUE)
    @SecurityRequirement(name = "none")
    @Operation(summary = "특정 사용자의 글 리스트")
    public RsData<PageDto<PostDto>> getPostsByUsername(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page
    ) {
        Member member = memberService.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.MEMBER_NOT_FOUND.getMessage()));

        Pageable pageable = PageRequest.of(page, AppConfig.getBasePageSize());

        Page<Post> postEntities = postService.findByAuthorIdOrderByIdDesc(member.getId(), pageable);
        List<PostDto> postDtos = postEntities.stream().map(PostDto::new).toList();
        Page<PostDto> pagePosts = new PageImpl<>(postDtos, pageable, postEntities.getTotalElements());

        return RsData.of(
                "200",
                SuccessMessage.GET_POSTS_BY_USERNAME_SUCCESS.getMessage().formatted(member.getUsername()),
                new PageDto<>(pagePosts)
        );
    }

    @GetMapping(value = "/{username}/{id}", consumes = ALL_VALUE)
    @Operation(summary = "특정 사용자의 글 상세 조회")
    public RsData<PostDto> getPostById(
            @PathVariable String username,
            @PathVariable long id
    ) {
        Member member = memberService.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.MEMBER_NOT_FOUND.getMessage()));
        Post post = postService.findByIdAndAuthorId(id, member.getId()).orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.POST_NOT_FOUND.getMessage()));
        if (!postService.canAccess(rq.getMember(), post)) throw new CustomAccessDeniedException(ErrorMessage.NO_ACCESS.getMessage());

        PostDto postDto = new PostDto(post);

        return RsData.of(
                "200",
                SuccessMessage.GET_POST_BY_USERNAME_SUCCESS.getMessage().formatted(member.getUsername(), post.getId()),
                postDto
        );
    }
}
