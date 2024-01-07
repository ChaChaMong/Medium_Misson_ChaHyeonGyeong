package com.ll.medium.global.security;

import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.global.rq.Rq;
import com.ll.medium.global.rsData.RsData;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final MemberService memberService;
    private final Rq rq;

    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        if (!request.getRequestURI().startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String uri = request.getRequestURI();

        if (uri.matches("/api/.*/members/(login|join)")) {
            filterChain.doFilter(request, response);
            return;
        }

        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null) {
            String refreshToken = bearerToken.substring("Bearer ".length());
            RsData<String> rs = memberService.refreshAccessToken(refreshToken);
            String accessToken = rs.getData();
            SecurityUser securityUser = memberService.getUserFromAccessToken(accessToken);
            rq.setLogin(securityUser);
        } else {
            String accessToken = rq.getCookieValue("accessToken", "");

            if (!accessToken.isBlank()) {
                if (!memberService.validateToken(accessToken)) {
                    String refreshToken = rq.getCookieValue("refreshToken", "");

                    RsData<String> rs = memberService.refreshAccessToken(refreshToken);
                    accessToken = rs.getData();
                    rq.setCrossDomainCookie("accessToken", accessToken);
                }

                SecurityUser securityUser = memberService.getUserFromAccessToken(accessToken);
                rq.setLogin(securityUser);
            }
        }

        filterChain.doFilter(request, response);
    }
}