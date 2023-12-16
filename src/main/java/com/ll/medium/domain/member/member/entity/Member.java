package com.ll.medium.domain.member.member.entity;

import com.ll.medium.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@SuperBuilder
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Setter
@Getter
@ToString(callSuper = true)
public class Member extends BaseEntity {
    private String username;
    private String password;

    public boolean isAdmin() {
        return username.equals("admin");
    }

//    public List<SimpleGrantedAuthority> getAuthorities() {
//        if (isAdmin()) {
//            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_MEMBER"));
//        }
//
//        return List.of(new SimpleGrantedAuthority("ROLE_MEMBER"));
//    }

    public List<? extends GrantedAuthority> getAuthorities() {
        return getAuthoritiesAsStrList()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    public List<String> getAuthoritiesAsStrList() {
        return List.of("ROLE_MEMBER");
    }
}
