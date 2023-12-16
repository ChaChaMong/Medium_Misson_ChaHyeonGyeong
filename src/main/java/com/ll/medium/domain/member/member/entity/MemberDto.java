package com.ll.medium.domain.member.member.entity;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberDto {
    private final Long id;
    private final LocalDateTime createDate;
    private final LocalDateTime modifyDate;
    private final String username;

    public MemberDto(Member member) {
        this.id = member.getId();
        this.createDate = member.getCreateDate();
        this.modifyDate = member.getModifyDate();
        this.username = member.getUsername();
    }
}
