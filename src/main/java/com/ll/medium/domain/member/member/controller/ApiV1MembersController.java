package com.ll.medium.domain.member.member.controller;

import com.ll.medium.domain.member.member.dto.JoinRequestDto;
import com.ll.medium.domain.member.member.dto.LoginRequestDto;
import com.ll.medium.domain.member.member.dto.LoginResponseDto;
import com.ll.medium.domain.member.member.dto.MemberDto;
import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.service.AuthTokenService;
import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.global.common.ErrorMessage;
import com.ll.medium.global.common.SuccessMessage;
import com.ll.medium.global.exception.ResourceNotFoundException;
import com.ll.medium.global.rq.Rq;
import com.ll.medium.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/members", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "ApiV1MembersController", description = "계정 관련 컨트롤러")
@SecurityRequirement(name = "bearerAuth")
public class ApiV1MembersController {
    private final MemberService memberService;
    private final AuthTokenService authTokenService;
    private final Rq rq;

    @PostMapping(value = "/login")
    @Operation(summary = "로그인, 로그인 성공시 accessToken, refreshToken 쿠키 설정")
    public RsData<?> login(
            @Valid @RequestBody LoginRequestDto loginRequestDto
    ) {
        Optional<Member> memberOp = memberService.findByUsername(loginRequestDto.getUsername());

        if (!memberService.checkUsernameAndPassword(memberOp, loginRequestDto.getPassword())) {
            throw new ResourceNotFoundException(ErrorMessage.LOGIN_FAIL.getMessage());
        }

        Member member = memberOp.get();

        String refreshToken = authTokenService.genRefreshToken(member);
        String accessToken = authTokenService.genAccessToken(member);

        rq.setCrossDomainCookie("refreshToken", refreshToken);
        rq.setCrossDomainCookie("accessToken", accessToken);

        return RsData.of(
                "200",
                SuccessMessage.LOGIN_SUCCESS.getMessage(),
                new LoginResponseDto(member, refreshToken, accessToken)
        );
    }

    @PostMapping("/join")
    @Operation(summary = "회원가입")
    public RsData<?> join(
            @Valid @RequestBody JoinRequestDto joinRequestDto
    ) {
        if (!joinRequestDto.isPasswordConfirm()) {
            throw new ResourceNotFoundException(ErrorMessage.NOT_MATCH_PASSWORD.getMessage());
        }

        if (memberService.existsByUsername(joinRequestDto.getUsername())) {
            throw new ResourceNotFoundException(ErrorMessage.EXIST_USERNAME.getMessage());
        }

        Member member = memberService.join(joinRequestDto.getUsername(), joinRequestDto.getPassword());

        return RsData.of(
                "200",
                SuccessMessage.JOIN_SUCCESS.getMessage(),
                new MemberDto(member)
        );
    }
}
