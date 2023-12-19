package com.ll.medium.domain.member.member.controller;

import com.ll.medium.domain.member.member.dto.JoinForm;
import com.ll.medium.domain.member.member.dto.LoginResponseDto;
import com.ll.medium.domain.member.member.dto.MemberDto;
import com.ll.medium.domain.member.member.dto.MemberForm;
import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.global.common.ErrorMessage;
import com.ll.medium.global.common.SuccessMessage;
import com.ll.medium.global.exception.ResourceNotFoundException;
import com.ll.medium.global.rsData.RsData;
import com.ll.medium.global.util.jwt.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class ApiV1MembersController {
    private final MemberService memberService;

    @PostMapping("/login")
    public RsData<?> login(
            @RequestBody MemberForm memberForm
    ) {
        Optional<Member> memberOp = memberService.findByUsername(memberForm.getUsername());

        if (!memberService.checkUsernameAndPassword(memberOp, memberForm.getPassword())) {
            throw new ResourceNotFoundException(ErrorMessage.LOGIN_FAIL.getMessage());
        }

        Member member = memberOp.get();

        Long id = member.getId();
        String accessToken = JwtUtil.encode(
                Map.of(
                        "id", id.toString(),
                        "username", member.getUsername(),
                        "authorities", member.getAuthoritiesAsStrList()
                )
        );

        return RsData.of(
                "200",
                SuccessMessage.LOGIN_SUCCESS.getMessage(),
                new LoginResponseDto(member, accessToken)
        );
    }

    @PostMapping("/join")
    public RsData<?> join(
            @Valid @RequestBody JoinForm joinForm
    ) {
        if (!joinForm.isPasswordConfirm()) {
            throw new ResourceNotFoundException(ErrorMessage.NOT_MATCH_PASSWORD.getMessage());
        }

        Member member = memberService.join(Member.builder()
                .username(joinForm.getUsername())
                .password(joinForm.getPassword())
                .build());

        return RsData.of(
                "200",
                SuccessMessage.JOIN_SUCCESS.getMessage(),
                new MemberDto(member)
        );
    }
}
