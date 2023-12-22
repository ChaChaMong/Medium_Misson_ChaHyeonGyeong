package com.ll.medium.domain.member.member.dto;

import com.ll.medium.domain.member.member.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginResponseDto {
    private MemberDto item;
    private String refreshToken;
    private String accessToken;

    public LoginResponseDto(Member member, String refreshToken, String accessToken) {
        this.item = new MemberDto(member);
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }
}
