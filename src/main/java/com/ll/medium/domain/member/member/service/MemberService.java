package com.ll.medium.domain.member.member.service;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.repository.MemberRepository;
import com.ll.medium.global.security.SecurityUser;
import com.ll.medium.global.util.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member join(String username, String password) {
        Member encodingMember = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();

        return memberRepository.save(encodingMember);
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public long count() {
        return memberRepository.count();
    }

    public Optional<Member> findById(long id) {
        return memberRepository.findById(id);
    }

    public SecurityUser getUserFromApiKey(String apiKey) {
        Claims claims = JwtUtil.decode(apiKey);

        Map<String, Object> data = (Map<String, Object>) claims.get("data");
        long id = Long.parseLong(data.get("id").toString());
        String username = (String) data.get("username");

        List<? extends GrantedAuthority> authorities = ((List<String>) data.get("authorities"))
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new
                SecurityUser(
                id,
                username,
                "",
                authorities
        );
    }

    public boolean checkUsernameAndPassword(Optional<Member> memberOp, String password) {
        return memberOp.isPresent() && passwordEncoder.matches(password, memberOp.get().getPassword());
    }

    public boolean existsByUsername(String username) {
        return memberRepository.existsByUsername(username);
    }
}
