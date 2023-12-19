package com.ll.medium.domain.member.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinRequestDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String passwordConfirm;

    public boolean isPasswordConfirm() {
        return password.equals(passwordConfirm);
    }
}
