package com.ll.medium.domain.post.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostForm {
    @NotBlank
    private String title;
    @NotBlank
    private String body;
    private boolean isPublished;
}
