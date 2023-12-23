package com.ll.medium.domain.member.member.service;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.global.util.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthTokenService {
    public String genToken(Member member, long expireSeconds) {

        return JwtUtil.encode(
                Map.of(
                        "id", member.getId(),
                        "username", member.getUsername(),
                        "authorities", member.getAuthoritiesAsStringList()
                ),
                expireSeconds
        );
    }

    public String genRefreshToken(Member member) {
        return genToken(member, 60 * 60 * 24 * 365);
    }

    public String genAccessToken(Member member) {
        return genToken(member, 60 * 10);
    }
}
