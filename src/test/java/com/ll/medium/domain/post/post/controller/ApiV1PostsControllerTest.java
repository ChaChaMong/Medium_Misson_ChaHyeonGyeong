package com.ll.medium.domain.post.post.controller;

import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ApiV1PostsControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private PostService postService;
    // 날짜 패턴 정규식
    private static final String DATE_PATTERN = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.?\\d{0,7}";

    @Test
    @DisplayName("GET /api/v1/posts")
    void t1() throws Exception {
        //When
        ResultActions resultActions = mvc
                .perform(get("/api/v1/posts"))
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1PostsController.class))
                .andExpect(handler().methodName("getPosts"))
                .andExpect(jsonPath("$.data.items[0].id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.items[0].createDate", matchesPattern(DATE_PATTERN)))
                .andExpect(jsonPath("$.data.items[0].modifyDate", matchesPattern(DATE_PATTERN)))
                .andExpect(jsonPath("$.data.items[0].authorId", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.items[0].authorName", notNullValue()))
                .andExpect(jsonPath("$.data.items[0].title", notNullValue()))
                .andExpect(jsonPath("$.data.items[0].body", notNullValue()))
                .andExpect(jsonPath("$.data.items[0].published", instanceOf(Boolean.class)));
    }

    @Test
    @DisplayName("GET /api/v1/posts/myList")
    @WithUserDetails("user1")
    void t1_2() throws Exception {
        //When
        ResultActions resultActions = mvc
                .perform(get("/api/v1/posts/myList"))
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1PostsController.class))
                .andExpect(handler().methodName("getMyPosts"))
                .andExpect(jsonPath("$.data.items[0].id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.items[0].createDate", matchesPattern(DATE_PATTERN)))
                .andExpect(jsonPath("$.data.items[0].modifyDate", matchesPattern(DATE_PATTERN)))
                .andExpect(jsonPath("$.data.items[0].authorId", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.items[0].authorName", notNullValue()))
                .andExpect(jsonPath("$.data.items[0].title", notNullValue()))
                .andExpect(jsonPath("$.data.items[0].body", notNullValue()))
                .andExpect(jsonPath("$.data.items[0].published", instanceOf(Boolean.class)));
    }

    @Test
    @DisplayName("GET /api/v1/posts/1 - 공개 게시글 비로그인")
    void t2_1() throws Exception {
        //When
        ResultActions resultActions = mvc
                .perform(get("/api/v1/posts/1"))
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1PostsController.class))
                .andExpect(handler().methodName("getPost"))
                .andExpect(status().isOk())  // HTTP 상태 코드가 200인지 확인합니다.
                .andExpect(jsonPath("$.data.item.id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.item.createDate", matchesPattern(DATE_PATTERN)))
                .andExpect(jsonPath("$.data.item.modifyDate", matchesPattern(DATE_PATTERN)))
                .andExpect(jsonPath("$.data.item.authorId", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.item.authorName", notNullValue()))
                .andExpect(jsonPath("$.data.item.title", notNullValue()))
                .andExpect(jsonPath("$.data.item.body", notNullValue()))
                .andExpect(jsonPath("$.data.item.published", instanceOf(Boolean.class)));
    }

    @Test
    @DisplayName("GET /api/v1/posts/1 - 비공개 게시글 로그인")
    @WithUserDetails("user1")
    void t2_2() throws Exception {
        //When
        ResultActions resultActions = mvc
                .perform(get("/api/v1/posts/1"))
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1PostsController.class))
                .andExpect(handler().methodName("getPost"))
                .andExpect(status().isOk())  // HTTP 상태 코드가 200인지 확인합니다.
                .andExpect(jsonPath("$.data.item.id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.item.createDate", matchesPattern(DATE_PATTERN)))
                .andExpect(jsonPath("$.data.item.modifyDate", matchesPattern(DATE_PATTERN)))
                .andExpect(jsonPath("$.data.item.authorId", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.item.authorName", notNullValue()))
                .andExpect(jsonPath("$.data.item.title", notNullValue()))
                .andExpect(jsonPath("$.data.item.body", notNullValue()))
                .andExpect(jsonPath("$.data.item.published", instanceOf(Boolean.class)));
    }

    @Test
    @DisplayName("GET /api/v1/posts/1000 - 게시물을 찾을 수 없음")
    void t2_3() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(get("/api/v1/posts/1000"))
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1PostsController.class))
                .andExpect(handler().methodName("getPost"))
                .andExpect(jsonPath("$.resultCode", is("404")))  // resultCode가 "404"인지 확인합니다.
                .andExpect(jsonPath("$.msg", is("해당 게시물을 찾을 수 없습니다.")));  // 메시지가 올바른지 확인합니다.
    }

    @Test
    @DisplayName("GET /api/v1/posts/20 - 조회권한이 없음 (비로그인)")
    void t2_4() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(get("/api/v1/posts/20"))
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1PostsController.class))
                .andExpect(handler().methodName("getPost"))
                .andExpect(jsonPath("$.resultCode", is("403")))
                .andExpect(jsonPath("$.msg", is("조회권한이 없습니다.")));
    }

    @Test
    @DisplayName("GET /api/v1/posts/20 - 조회권한이 없음 (로그인)")
    @WithUserDetails("user2")
    void t2_5() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(get("/api/v1/posts/20"))
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1PostsController.class))
                .andExpect(handler().methodName("getPost"))
                .andExpect(jsonPath("$.resultCode", is("403")))
                .andExpect(jsonPath("$.msg", is("조회권한이 없습니다.")));
    }

    @Test
    @DisplayName("DELETE /api/v1/posts/1")
    void t3() throws Exception {
        //When
        ResultActions resultActions = mvc
                .perform(delete("/api/v1/posts/1"))
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1PostsController.class))
                .andExpect(handler().methodName("deletePost"))
                .andExpect(jsonPath("$.data.item.id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.item.createDate", matchesPattern(DATE_PATTERN)))
                .andExpect(jsonPath("$.data.item.modifyDate", matchesPattern(DATE_PATTERN)))
                .andExpect(jsonPath("$.data.item.authorId", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.item.authorName", notNullValue()))
                .andExpect(jsonPath("$.data.item.title", notNullValue()))
                .andExpect(jsonPath("$.data.item.body", notNullValue()))
                .andExpect(jsonPath("$.data.item.published", instanceOf(Boolean.class)));

        Post post = postService.findById(1L).orElse(null);
        assertThat(post).isNull();
    }

    @Test
    @DisplayName("PUT /api/v1/posts/1")
    void t4() throws Exception {
        //When
        ResultActions resultActions = mvc
                .perform(
                        put("/api/v1/posts/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "title": "제목1-수정",
                                            "body": "내용1-수정",
                                            "isPublished": false
                                        }
                                        """)
                )
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1PostsController.class))
                .andExpect(handler().methodName("modifyPost"))
                .andExpect(jsonPath("$.data.item.id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.item.createDate", matchesPattern(DATE_PATTERN)))
                .andExpect(jsonPath("$.data.item.modifyDate", matchesPattern(DATE_PATTERN)))
                .andExpect(jsonPath("$.data.item.authorId", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.item.authorName", notNullValue()))
                .andExpect(jsonPath("$.data.item.title", is("제목1-수정")))
                .andExpect(jsonPath("$.data.item.body", is("내용1-수정")))
                .andExpect(jsonPath("$.data.item.published", is(false)));
    }

    @Test
    @DisplayName("POST /api/v1/posts")
    @WithUserDetails("user1")
    void t5() throws Exception {
        //When
        ResultActions resultActions = mvc
                .perform(
                        post("/api/v1/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "title": "제목-등록",
                                            "body": "내용-등록",
                                            "published": true
                                        }
                                        """)
                )
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1PostsController.class))
                .andExpect(handler().methodName("writePost"))
                .andExpect(jsonPath("$.data.item.id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.item.createDate", matchesPattern(DATE_PATTERN)))
                .andExpect(jsonPath("$.data.item.modifyDate", matchesPattern(DATE_PATTERN)))
                .andExpect(jsonPath("$.data.item.authorId", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.item.authorName", notNullValue()))
                .andExpect(jsonPath("$.data.item.title", is("제목-등록")))
                .andExpect(jsonPath("$.data.item.body", is("내용-등록")))
                .andExpect(jsonPath("$.data.item.published", is(true)));
    }
}
