package com.ll.medium.domain.post.post.dto;

import com.ll.medium.domain.post.post.entity.Post;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
public class PostListDto {
    @NonNull
    private final Long id;
    @NonNull
    private final String authorName;
    @NonNull
    private final String title;
    @NonNull
    private final boolean isPublished;
    @NonNull
    private final boolean isPaid;

    public PostListDto(Post post) {
        this.id = post.getId();
        this.authorName = post.getAuthor().getUsername();
        this.title  = post.getTitle();
        this.isPublished = post.isPublished();
        this.isPaid = post.isPaid();
    }
}
