package com.ll.medium.domain.member.member.dto;

import com.ll.medium.domain.member.member.entity.Member;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
public class LoginResponseDto {
    @NonNull
    private final MemberDto item;
    @NonNull
    private final String refreshToken;
    @NonNull
    private final String accessToken;

    public LoginResponseDto(Member member, String refreshToken, String accessToken) {
        this.item = new MemberDto(member);
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }
}
