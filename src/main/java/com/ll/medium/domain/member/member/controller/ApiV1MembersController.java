package com.ll.medium.domain.member.member.controller;

import com.ll.medium.domain.member.member.dto.JoinRequestDto;
import com.ll.medium.domain.member.member.dto.LoginRequestDto;
import com.ll.medium.domain.member.member.dto.LoginResponseDto;
import com.ll.medium.domain.member.member.dto.MemberDto;
import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.service.AuthTokenService;
import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.global.common.Message;
import com.ll.medium.global.exception.ResourceNotFoundException;
import com.ll.medium.global.rq.Rq;
import com.ll.medium.global.rsData.RsData;
import com.ll.medium.standard.base.Empty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.util.MimeTypeUtils.ALL_VALUE;

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
    public RsData<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto loginRequestDto
    ) {
        Optional<Member> memberOp = memberService.findByUsername(loginRequestDto.getUsername());

        if (!memberService.checkUsernameAndPassword(memberOp, loginRequestDto.getPassword())) {
            throw new ResourceNotFoundException(Message.Error.LOGIN_FAIL.getMessage());
        }

        Member member = memberOp.get();

        String refreshToken = member.getRefreshToken();
        String accessToken = authTokenService.genAccessToken(member);

        rq.setCrossDomainCookie("refreshToken", refreshToken);
        rq.setCrossDomainCookie("accessToken", accessToken);

        return RsData.of(
                "200",
                Message.Success.LOGIN_SUCCESS.getMessage(),
                new LoginResponseDto(member, refreshToken, accessToken)
        );
    }

    @PostMapping("/join")
    @Operation(summary = "회원가입")
    public RsData<MemberDto> join(
            @Valid @RequestBody JoinRequestDto joinRequestDto
    ) {
        if (!joinRequestDto.isPasswordConfirm()) {
            throw new ResourceNotFoundException(Message.Error.NOT_MATCH_PASSWORD.getMessage());
        }

        if (memberService.existsByUsername(joinRequestDto.getUsername())) {
            throw new ResourceNotFoundException(Message.Error.EXIST_USERNAME.getMessage());
        }

        Member member = memberService.join(joinRequestDto.getUsername(), joinRequestDto.getPassword());

        return RsData.of(
                "200",
                Message.Success.JOIN_SUCCESS.getMessage(),
                new MemberDto(member)
        );
    }

    @GetMapping(value = "/me", consumes = ALL_VALUE)
    @Operation(summary = "내 정보")
    public RsData<MemberDto> getMe() {
        return RsData.of(
                "200",
                Message.Success.GET_MY_PROFILE_SUCCESS.getMessage(),
                new MemberDto(rq.getMember())
        );
    }

    @PostMapping(value = "/logout", consumes = ALL_VALUE)
    @Operation(summary = "로그아웃")
    public RsData<Empty> logout() {
        rq.setLogout();

        return RsData.of(
                "200",
                Message.Success.LOGOUT_SUCCESS.getMessage()
        );
    }
}
