package com.ll.medium.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {
    NOT_LOGGED_IN("로그인이 되어있지 않습니다."),
    POST_NOT_FOUND("해당 게시물을 찾을 수 없습니다."),
    NO_ACCESS("조회권한이 없습니다."),
    NO_MODIFY_PERMISSION("수정권한이 없습니다."),
    NO_DELETE_PERMISSION("삭제권한이 없습니다.");

    private final String message;
}