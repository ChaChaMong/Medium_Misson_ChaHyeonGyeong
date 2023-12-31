package com.ll.medium.global.initData;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
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
    @Order(3)
    public ApplicationRunner initNotProdData() {
        return args -> {
            self.work1();
        };
    }

    @Transactional
    public void work1() {
        if (memberService.findByUsername("user1").isPresent()) return;

        Member member1 = memberService.join("user1", "1234");
        Member member2 = memberService.join("user2", "1234");

        memberService.setIsPaid(member1, true);

        IntStream.rangeClosed(1, 10).forEach(
                i -> {
                    String title = "제목" + i;
                    String body = "내용" + i;
                    Post post = postService.write(member1, title, body, true);
                    if (i % 2 == 0) postService.setIsPaid(post, true);
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
                    Post post = postService.write(member2, title, body, true);
                    if (i % 2 == 0) postService.setIsPaid(post, true);
                }
        );


        IntStream.rangeClosed(3, 100).forEach(
                i -> {
                    Member member = memberService.join("user" + i, "1234");
                    member.setPaid(true);

                    String title = member.getUsername() + "의 유료글 제목";
                    String body = member.getUsername() + "의 유료글 내용";
                    Post post = postService.write(member, title, body, true);
                    postService.setIsPaid(post, true);
                }
        );
    }
}