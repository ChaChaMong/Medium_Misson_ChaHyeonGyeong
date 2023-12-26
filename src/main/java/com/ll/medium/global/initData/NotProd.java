package com.ll.medium.global.initData;

import com.ll.medium.domain.post.post.service.PostService;
import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@Profile("!prod")
@Configuration
@RequiredArgsConstructor
public class NotProd {
    @Autowired
    @Lazy
    private NotProd self;
    private final MemberService memberService;
    private final PostService postService;

    @Bean
    public ApplicationRunner initNotProdData() {
        return args -> {
            self.work1();
        };
    }

    @Transactional
    public void work1() {
        if (memberService.count() > 0) return;

        Member admin = memberService.join("admin", "1234");
        Member member1 = memberService.join("user1", "1234");
        Member member2 = memberService.join("user2", "1234");

        IntStream.rangeClosed(1, 10).forEach(
                i -> {
                    String title = "제목" + i;
                    String body = "내용" + i;
                    postService.write(member1, title, body, true);
                }
        );

        IntStream.rangeClosed(11, 20).forEach(
                i -> {
                    String title = "제목" + i;
                    String body = "내용" + i;
                    postService.write(member1, title, body, false);
                }
        );

        IntStream.rangeClosed(21, 40).forEach(
                i -> {
                    String title = "제목" + i;
                    String body = "내용" + i;
                    postService.write(member2, title, body, true);
                }
        );

        IntStream.rangeClosed(41, 50).forEach(
                i -> {
                    String title = "제목" + i;
                    String body = "내용" + i;
                    postService.write(admin, title, body, false);
                }
        );
    }
}