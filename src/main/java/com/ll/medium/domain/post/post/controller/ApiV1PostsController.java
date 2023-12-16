package com.ll.medium.domain.post.post.controller;

import com.ll.medium.domain.post.post.dto.PostDto;
import com.ll.medium.domain.post.post.dto.PostForm;
import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.service.PostService;
import com.ll.medium.global.rq.Rq;
import com.ll.medium.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class ApiV1PostsController {
    private final PostService postService;
    private final Rq rq;

    @Getter
    public static class GetPostsResponseBody {
        private final List<PostDto> items;

        public GetPostsResponseBody(Page<Post> posts) {
            items = posts
                    .stream()
                    .map(PostDto::new)
                    .toList();
        }
    }

    @GetMapping("")
    public RsData<GetPostsResponseBody> getPosts(
            @RequestParam(value="page",
                    defaultValue="0") int page
    ) {
        return RsData.of(
                "200",
                "성공",
                new GetPostsResponseBody(
                        postService.findByIsPublishedOrderByIdDesc(true, page)
                )
        );
    }

    //@PreAuthorize("isAuthenticated()")
    @GetMapping("/myList")
    public RsData<GetPostsResponseBody> getMyPosts(
            @RequestParam(value="page",
                    defaultValue="0") int page
    ) {
        return RsData.of(
                "200",
                "성공",
                new GetPostsResponseBody(
                        postService.findByAuthorIdOrderByIdDesc(rq.getMemberDump().getId(), page)
                )
        );
    }

    @Getter
    public static class GetPostResponseBody {
        private final PostDto item;

        public GetPostResponseBody(Post post) {
            item = new PostDto(post);
        }
    }

    @GetMapping("/{id}")
    public RsData<GetPostResponseBody> getPost(
            @PathVariable long id
    ) {
        return RsData.of(
                "200",
                "성공",
                new GetPostResponseBody(
                        postService.findById(id).get()
                )
        );

//        Post post = postService.findById(id).orElseThrow(() -> new RuntimeException("해당 게시물을 찾을 수 없습니다."));
//
//        if (!postService.canAccess(rq.getMember(), post)) throw new RuntimeException("조회권한이 없습니다.");
//
//        return post;
    }

    @Getter
    public static class WritePostResponseBody {
        private final PostDto item;

        public WritePostResponseBody(Post post) {
            item = new PostDto(post);
        }
    }

    //@PreAuthorize("isAuthenticated()")
    @PostMapping
    public RsData<WritePostResponseBody> writePost(
            @Valid @RequestBody PostForm postForm
    ) {
        Post post = postService.write(rq.getMember(), postForm.getTitle(), postForm.getBody(), postForm.isPublished());

        RsData<Post> writeRs = RsData.of("200", "%d번 게시글이 작성되었습니다.".formatted(post.getId()), post);

        return writeRs.of(
                new WritePostResponseBody(
                        writeRs.getData()
                )
        );
    }

    @Getter
    public static class ModifyPostResponseBody {
        private final PostDto item;

        public ModifyPostResponseBody(Post post) {
            item = new PostDto(post);
        }
    }

    //@PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public RsData<ModifyPostResponseBody> modifyPost(
            @PathVariable long id,
            @Valid @RequestBody PostForm postForm
    ) {
        Post post = postService.findById(id).get();

        postService.modify(post, postForm.getTitle(), postForm.getBody(), postForm.isPublished());

        RsData<Post> modifyRs = RsData.of("200", "%d번 게시글이 수정되었습니다.".formatted(post.getId()), post);

        return modifyRs.of(
                new ModifyPostResponseBody(
                        modifyRs.getData()
                )
        );

//        Post post = postService.findById(id).orElseThrow(() -> new RuntimeException("해당 게시물을 찾을 수 없습니다."));
//
//        if (!postService.canModify(rq.getMember(), post)) throw new RuntimeException("수정권한이 없습니다.");
//
//        postService.modify(post, postForm.getTitle(), postForm.getBody(), postForm.isPublished());
//
//        return post;
    }

    @Getter
    public static class DeletePostResponseBody {
        private final PostDto item;

        public DeletePostResponseBody(Post post) {
            item = new PostDto(post);
        }
    }

    //@PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public RsData<DeletePostResponseBody> deletePost(
            @PathVariable long id
    ) {
        Post post = postService.findById(id).get();

        postService.delete(post);

        RsData<Post> deleteRs = RsData.of("200", "%d번 게시글이 삭제되었습니다.".formatted(post.getId()), post);

        return deleteRs.of(
                new DeletePostResponseBody(
                        deleteRs.getData()
                )
        );
//        Post post = postService.findById(id).orElseThrow(() -> new RuntimeException("해당 게시물을 찾을 수 없습니다."));
//
//        if (!postService.canDelete(rq.getMember(), post)) throw new RuntimeException("삭제권한이 없습니다.");
//
//        postService.delete(post);
    }
}
