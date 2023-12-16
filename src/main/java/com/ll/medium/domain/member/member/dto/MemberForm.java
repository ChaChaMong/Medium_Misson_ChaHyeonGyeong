package com.ll.medium.domain.member.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberForm {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
