package com.ll.medium.domain.post.blog.dto;

import com.ll.medium.domain.post.post.entity.Post;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
public class BlogListDto {
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
    @NonNull
    private final boolean canAccess;

    public BlogListDto(Post post, boolean canAccess) {
        this.id = post.getId();
        this.authorName = post.getAuthor().getUsername();
        this.title  = post.getTitle();
        this.isPublished = post.isPublished();
        this.isPaid = post.isPaid();
        this.canAccess = canAccess;
    }
}
