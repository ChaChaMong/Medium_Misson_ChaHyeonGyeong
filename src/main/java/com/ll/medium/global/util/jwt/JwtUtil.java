package com.ll.medium.global.util.jwt;

import com.ll.medium.global.app.AppConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtil {

    public static String encode(Map<String, Object> data, long expireSeconds) {
        Claims claims = Jwts
                .claims()
                .add("data", data)
                .build();

        Date now = new Date();
        Date validity = new Date(now.getTime() + 1000 * expireSeconds); // 5분 만료시간

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, AppConfig.getJwtSecretKey())
                .compact();
    }

    public static Claims decode(String token) {
        return Jwts
                .parser()
                .setSigningKey(AppConfig.getJwtSecretKey())
                .build()
                .parseClaimsJws(token)
                .getPayload();
    }
}
