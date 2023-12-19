package com.ll.medium.domain.post.blog.controller;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.domain.post.post.dto.PostDto;
import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.service.PostService;
import com.ll.medium.global.common.ErrorMessage;
import com.ll.medium.global.common.SuccessMessage;
import com.ll.medium.global.exception.CustomAccessDeniedException;
import com.ll.medium.global.exception.ResourceNotFoundException;
import com.ll.medium.global.rq.Rq;
import com.ll.medium.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/b")
@RequiredArgsConstructor
public class ApiV1BlogsController {
    private final PostService postService;
    private final MemberService memberService;
    private final Rq rq;

    @GetMapping("/{username}")
    public RsData<?> getPostsByUsername(
            @PathVariable String username,
            @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        Member member = memberService.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.MEMBER_NOT_FOUND.getMessage()));
        Page<Post> postEntities = postService.findByAuthorIdOrderByIdDesc(member.getId(), page);
        List<PostDto> postDtos = postEntities.stream().map(PostDto::new).toList();

        return RsData.of(
                "200",
                SuccessMessage.GET_POSTS_BY_USERNAME_SUCCESS.getMessage().formatted(member.getUsername()),
                postDtos
        );
    }

    @GetMapping("/{username}/{id}")
    public RsData<?> getPostById(
            @PathVariable String username,
            @PathVariable long id
    ) {
        Member member = memberService.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.MEMBER_NOT_FOUND.getMessage()));
        Post post = postService.findById(id).orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.POST_NOT_FOUND.getMessage()));
        if (!postService.canAccess(rq.getMember(), post)) throw new CustomAccessDeniedException(ErrorMessage.NO_ACCESS.getMessage());

        PostDto postDto = new PostDto(post);

        return RsData.of(
                "200",
                SuccessMessage.GET_POST_BY_USERNAME_SUCCESS.getMessage().formatted(member.getUsername(), post.getId()),
                postDto
        );
    }
}
