package com.ll.medium.domain.post.post.dto;

import com.ll.medium.domain.post.post.entity.Post;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Getter
public class BlogListDto {
    @NonNull
    private final Long id;
    @NonNull
    private final LocalDateTime createDate;
    @NonNull
    private final LocalDateTime modifyDate;
    @NonNull
    private final Long authorId;
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
        this.createDate = post.getCreateDate();
        this.modifyDate = post.getModifyDate();
        this.authorId = post.getAuthor().getId();
        this.authorName = post.getAuthor().getUsername();
        this.title  = post.getTitle();
        this.isPublished = post.isPublished();
        this.isPaid = post.isPaid();
        this.canAccess = canAccess;
    }
}
