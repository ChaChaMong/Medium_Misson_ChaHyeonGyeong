package com.ll.medium.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessMessage {
    GET_POSTS_SUCCESS("전체 글 목록 조회 성공"),
    GET_MY_POSTS_SUCCESS("나의 글 목록 조회 성공"),
    GET_POST_SUCCESS("%d번 게시글이 조회되었습니다."),
    WRITE_POST_SUCCESS("%d번 게시글이 작성되었습니다."),
    MODIFY_POST_SUCCESS("%d번 게시글이 수정되었습니다."),
    DELETE_POST_SUCCESS("%d번 게시글이 삭제되었습니다."),

    GET_POSTS_BY_USERNAME_SUCCESS("%s의 글 목록 조회 성공"),
    GET_POST_BY_USERNAME_SUCCESS("%s의 %d번 게시글이 조회되었습니다."),
    
    LOGIN_SUCCESS("로그인 성공");

    private final String message;
}