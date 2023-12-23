package com.ll.medium.domain.member.member.dto;

import com.ll.medium.domain.member.member.entity.Member;
import lombok.Getter;

@Getter
public class LoginResponseDto {
    private final MemberDto item;
    private final String refreshToken;
    private final String accessToken;

    public LoginResponseDto(Member member, String refreshToken, String accessToken) {
        this.item = new MemberDto(member);
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }
}
