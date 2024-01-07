package com.ll.medium.domain.post.blog.controller;

import com.ll.medium.domain.post.post.service.PostService;
import com.ll.medium.global.common.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ApiV1BlogsControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private PostService postService;
    // 날짜 패턴 정규식
    private static final String DATE_PATTERN = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.?\\d{0,7}";

    @Test
    @DisplayName("GET /api/v1/b/user1 - 200")
    void t0() throws Exception {
        //When
        ResultActions resultActions = mvc
                .perform(get("/api/v1/b/user1"))
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1BlogsController.class))
                .andExpect(handler().methodName("getPostsByUsername"))
                .andExpect(jsonPath("$.resultCode", is("200")))
                .andExpect(jsonPath("$.msg", is(Message.Success.GET_POSTS_BY_USERNAME_SUCCESS.getMessage().formatted("user1"))))
                .andExpect(jsonPath("$.data.content[0].id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.content[0].createDate", matchesPattern(DATE_PATTERN)))
                .andExpect(jsonPath("$.data.content[0].modifyDate", matchesPattern(DATE_PATTERN)))
                .andExpect(jsonPath("$.data.content[0].authorId", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.content[0].authorName", notNullValue()))
                .andExpect(jsonPath("$.data.content[0].title", notNullValue()))
                .andExpect(jsonPath("$.data.content[0].published", instanceOf(Boolean.class)))
                .andExpect(jsonPath("$.data.content[0].paid", instanceOf(Boolean.class)))
                .andExpect(jsonPath("$.data.content[0].canAccess", instanceOf(Boolean.class)));
    }

    @Test
    @DisplayName("GET /api/v1/b/user1/1 - 공개 게시글 200")
    void t1() throws Exception {
        //When
        ResultActions resultActions = mvc
                .perform(get("/api/v1/b/user1/1"))
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1BlogsController.class))
                .andExpect(handler().methodName("getPostById"))
                .andExpect(jsonPath("$.resultCode", is("200")))
                .andExpect(jsonPath("$.msg", is(Message.Success.GET_POST_BY_USERNAME_SUCCESS.getMessage().formatted("user1", 1))))
                .andExpect(jsonPath("$.data.id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.createDate", matchesPattern(DATE_PATTERN)))
                .andExpect(jsonPath("$.data.modifyDate", matchesPattern(DATE_PATTERN)))
                .andExpect(jsonPath("$.data.authorId", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.authorName", notNullValue()))
                .andExpect(jsonPath("$.data.title", notNullValue()))
                .andExpect(jsonPath("$.data.body", notNullValue()))
                .andExpect(jsonPath("$.data.published", instanceOf(Boolean.class)))
                .andExpect(jsonPath("$.data.paid", instanceOf(Boolean.class)));
    }

    @Test
    @DisplayName("GET /api/v1/b/user1/11 - 비공개 게시글 200 (로그인)")
    @WithUserDetails("user1")
    void t1_2() throws Exception {
        //When
        ResultActions resultActions = mvc
                .perform(get("/api/v1/b/user1/11"))
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1BlogsController.class))
                .andExpect(handler().methodName("getPostById"))
                .andExpect(jsonPath("$.resultCode", is("200")))
                .andExpect(jsonPath("$.msg", is(Message.Success.GET_POST_BY_USERNAME_SUCCESS.getMessage().formatted("user1", 11))))
                .andExpect(jsonPath("$.data.id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.createDate", matchesPattern(DATE_PATTERN)))
                .andExpect(jsonPath("$.data.modifyDate", matchesPattern(DATE_PATTERN)))
                .andExpect(jsonPath("$.data.authorId", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.authorName", notNullValue()))
                .andExpect(jsonPath("$.data.title", notNullValue()))
                .andExpect(jsonPath("$.data.body", notNullValue()))
                .andExpect(jsonPath("$.data.published", instanceOf(Boolean.class)))
                .andExpect(jsonPath("$.data.paid", instanceOf(Boolean.class)));
    }

    @Test
    @DisplayName("GET /api/v1/b/user1/11 - 비공개 게시글 403 (로그인)")
    @WithUserDetails("user2")
    void t1_3() throws Exception {
        //When
        ResultActions resultActions = mvc
                .perform(get("/api/v1/b/user1/11"))
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ApiV1BlogsController.class))
                .andExpect(handler().methodName("getPostById"))
                .andExpect(jsonPath("$.resultCode", is("403")))
                .andExpect(jsonPath("$.msg", is(Message.Error.NO_ACCESS.getMessage())));
    }

    @Test
    @DisplayName("GET /api/v1/b/user1/11 - 비공개 게시글 403 (비로그인)")
    void t1_4() throws Exception {
        //When
        ResultActions resultActions = mvc
                .perform(get("/api/v1/b/user1/11"))
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ApiV1BlogsController.class))
                .andExpect(handler().methodName("getPostById"))
                .andExpect(jsonPath("$.resultCode", is("403")))
                .andExpect(jsonPath("$.msg", is(Message.Error.NO_ACCESS.getMessage())));
    }

    @Test
    @DisplayName("GET /api/v1/b/user1/40 - 게시글 id와 작성자 id가 다를 시 404")
    void t1_5() throws Exception {
        //When
        ResultActions resultActions = mvc
                .perform(get("/api/v1/b/user1/40"))
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ApiV1BlogsController.class))
                .andExpect(handler().methodName("getPostById"))
                .andExpect(jsonPath("$.resultCode", is("404")))
                .andExpect(jsonPath("$.msg", is(Message.Error.POST_NOT_FOUND.getMessage())));
    }
}
