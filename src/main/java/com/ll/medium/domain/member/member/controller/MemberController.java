package com.ll.medium.domain.member.member.controller;

import com.ll.medium.domain.member.member.dto.MemberForm;
import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.global.rq.Rq;
import com.ll.medium.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
@Validated
public class MemberController {
    private final MemberService memberService;
    private final Rq rq;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    String showLogin() {
        return "domain/member/member/login";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    String showJoin() {
        return "domain/member/member/join";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/join")
    String join(@Valid MemberForm memberForm){
        RsData<Member> joinRs = memberService.join(memberForm.getUsername(), memberForm.getPassword());

        return rq.redirectOrBack("/member/login", joinRs);
    }
}
