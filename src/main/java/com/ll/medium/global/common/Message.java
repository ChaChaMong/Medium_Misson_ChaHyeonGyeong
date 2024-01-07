package com.ll.medium.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class Message {
    @Getter
    @RequiredArgsConstructor
    public enum Success {
        GET_POSTS_SUCCESS("전체 글 목록 조회 성공"),
        GET_LATEST_POSTS_SUCCESS("최신 글 목록 조회 성공"),
        GET_MY_POSTS_SUCCESS("나의 글 목록 조회 성공"),
        GET_POST_SUCCESS("%d번 게시글이 조회되었습니다."),
        WRITE_POST_SUCCESS("%d번 게시글이 작성되었습니다."),
        MODIFY_POST_SUCCESS("%d번 게시글이 수정되었습니다."),
        DELETE_POST_SUCCESS("%d번 게시글이 삭제되었습니다."),

        GET_POSTS_BY_USERNAME_SUCCESS("%s의 글 목록 조회 성공"),
        GET_POST_BY_USERNAME_SUCCESS("%s의 %d번 게시글이 조회되었습니다."),

        GET_CONTROL_PERMISSION_SUCCESS("%d번 게시글 제어 권한 조회 성공"),

        LOGIN_SUCCESS("로그인 성공"),
        JOIN_SUCCESS("가입 성공"),

        REFRESH_TOKEN_SUCCESS("토큰 갱신 성공"),

        GET_MY_PROFILE_SUCCESS("내 정보 가져오기 성공"),
        LOGOUT_SUCCESS("로그아웃 성공");

        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum Error {
        NOT_LOGGED_IN("로그인이 되어있지 않습니다."),
        POST_NOT_FOUND("해당 게시물을 찾을 수 없습니다."),
        NO_ACCESS("조회권한이 없습니다."),
        NO_MODIFY_PERMISSION("수정권한이 없습니다."),
        NO_DELETE_PERMISSION("삭제권한이 없습니다."),

        MEMBER_NOT_FOUND("해당 사용자를 찾을 수 없습니다."),
        LOGIN_FAIL("존재하지 않는 회원이거나 비밀번호가 일치하지 않습니다."),
        NOT_MATCH_PASSWORD("비밀번호가 서로 일치하지 않습니다."),
        EXIST_USERNAME("이미 사용중인 아이디입니다."),

        NOT_EXIST_REFRESH_TOKEN("존재하지 않는 리프레시 토큰입니다.");

        private final String message;
    }
}
