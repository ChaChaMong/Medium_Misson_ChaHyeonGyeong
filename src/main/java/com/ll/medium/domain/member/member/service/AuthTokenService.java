package com.ll.medium.domain.member.member.service;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.global.app.AppConfig;
import com.ll.medium.global.util.jwt.JwtUtil;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
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

    public String genRefreshToken() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[10];
        random.nextBytes(bytes);
        return bytes.toString();
    }

    public String genAccessToken(Member member) {
        return genToken(member, 60 * 10);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(AppConfig.getJwtSecretKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
