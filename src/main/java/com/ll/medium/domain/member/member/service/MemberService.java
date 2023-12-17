package com.ll.medium.domain.member.member.service;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.repository.MemberRepository;
import com.ll.medium.global.rsData.RsData;
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
    public RsData<Member> join(String username, String password) {
        if (findByUsername(username).isPresent()) {
            return RsData.of("400", "이미 사용중인 아이디입니다.");
        }

        password = passwordEncoder.encode(password);
        Member member = Member
                .builder()
                .username(username)
                .password(password)
                .build();

        memberRepository.save(member);

        return RsData.of("200", "%s님 가입을 환영합니다.".formatted(member.getUsername()), member);
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
        long id = Long.parseLong((String) data.get("id"));
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

//    public boolean checkUsername(String username) {
//        Optional<Member> memberOp = findByUsername(username);
//
//        if (memberOp.isEmpty()) {
//            return false;
//        }
//
//        return true;
//    }
//
//
//    public boolean checkPassword(String username, String password) {
//        Optional<Member> memberOp = findByUsername(username);
//
//        if (!passwordEncoder.matches(password, memberOp.get().getPassword())) {
//            return false;
//        }
//
//        return true;
//    }

    public boolean checkUsernameAndPassword(Optional<Member> memberOp, String username, String password) {

        return memberOp.isPresent() && passwordEncoder.matches(password, memberOp.get().getPassword());
    }
}
