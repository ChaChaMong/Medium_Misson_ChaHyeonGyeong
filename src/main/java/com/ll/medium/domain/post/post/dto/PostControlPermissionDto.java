package com.ll.medium.domain.post.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
@AllArgsConstructor
public class PostControlPermissionDto {
    @NonNull
    private final boolean canModify;
    @NonNull
    private final boolean canDelete;
}
