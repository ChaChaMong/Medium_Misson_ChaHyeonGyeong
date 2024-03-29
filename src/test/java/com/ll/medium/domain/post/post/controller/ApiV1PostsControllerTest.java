package com.ll.medium.domain.post.post.controller;

import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.service.PostService;
import com.ll.medium.global.common.Message;
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
    @DisplayName("GET /api/v1/posts - 200 (비로그인)")
    void t0() throws Exception {
        //When
        ResultActions resultActions = mvc
                .perform(get("/api/v1/posts/list"))
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1PostsController.class))
                .andExpect(handler().methodName("getPosts"))
                .andExpect(jsonPath("$.resultCode", is("200")))
                .andExpect(jsonPath("$.msg", is(Message.Success.GET_POSTS_SUCCESS.getMessage())))
                .andExpect(jsonPath("$.data.content[0].id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.content[0].authorName", notNullValue()))
                .andExpect(jsonPath("$.data.content[0].title", notNullValue()))
                .andExpect(jsonPath("$.data.content[0].published", instanceOf(Boolean.class)))
                .andExpect(jsonPath("$.data.content[0].paid", instanceOf(Boolean.class)));
    }

    @Test
    @DisplayName("GET /api/v1/posts/myList - 200 (로그인)")
    @WithUserDetails("user1")
    void t1_1() throws Exception {
        //When
        ResultActions resultActions = mvc
                .perform(get("/api/v1/posts/myList"))
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1PostsController.class))
                .andExpect(handler().methodName("getMyPosts"))
                .andExpect(jsonPath("$.resultCode", is("200")))
                .andExpect(jsonPath("$.msg", is(Message.Success.GET_MY_POSTS_SUCCESS.getMessage())))
                .andExpect(jsonPath("$.data.content[0].id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.content[0].authorName", notNullValue()))
                .andExpect(jsonPath("$.data.content[0].title", notNullValue()))
                .andExpect(jsonPath("$.data.content[0].published", instanceOf(Boolean.class)))
                .andExpect(jsonPath("$.data.content[0].paid", instanceOf(Boolean.class)));
    }

    @Test
    @DisplayName("GET /api/v1/posts/myList - 401 (비로그인)")
    void t1_2() throws Exception {
        //When
        ResultActions resultActions = mvc
                .perform(get("/api/v1/posts/myList"))
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ApiV1PostsController.class))
                .andExpect(handler().methodName("getMyPosts"))
                .andExpect(jsonPath("$.resultCode", is("401")))
                .andExpect(jsonPath("$.msg", is(Message.Error.NOT_LOGGED_IN.getMessage())));
    }

    @Test
    @DisplayName("GET /api/v1/posts/1 - 공개 게시글 200 (비로그인)")
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
                .andExpect(jsonPath("$.resultCode", is("200")))
                .andExpect(jsonPath("$.msg", is(Message.Success.GET_POST_SUCCESS.getMessage().formatted(1))))
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
    @DisplayName("GET /api/v1/posts/1 - 비공개 게시글 200 (로그인)")
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
                .andExpect(jsonPath("$.resultCode", is("200")))
                .andExpect(jsonPath("$.msg", is(Message.Success.GET_POST_SUCCESS.getMessage().formatted(1))))
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
    @DisplayName("GET /api/v1/posts/1000 - 404")
    void t2_3() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(get("/api/v1/posts/1000"))
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ApiV1PostsController.class))
                .andExpect(handler().methodName("getPost"))
                .andExpect(jsonPath("$.resultCode", is("404")))
                .andExpect(jsonPath("$.msg", is(Message.Error.POST_NOT_FOUND.getMessage())));
    }

    @Test
    @DisplayName("GET /api/v1/posts/20 - 비공개 게시글 403 (비로그인)")
    void t2_4() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(get("/api/v1/posts/20"))
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ApiV1PostsController.class))
                .andExpect(handler().methodName("getPost"))
                .andExpect(jsonPath("$.resultCode", is("403")))
                .andExpect(jsonPath("$.msg", is(Message.Error.NO_ACCESS.getMessage())));
    }

    @Test
    @DisplayName("GET /api/v1/posts/20 - 비공개 게시글 403 (로그인)")
    @WithUserDetails("user2")
    void t2_5() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(get("/api/v1/posts/20"))
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ApiV1PostsController.class))
                .andExpect(handler().methodName("getPost"))
                .andExpect(jsonPath("$.resultCode", is("403")))
                .andExpect(jsonPath("$.msg", is(Message.Error.NO_ACCESS.getMessage())));
    }

    @Test
    @DisplayName("DELETE /api/v1/posts/1 - 200 (로그인)")
    @WithUserDetails("user1")
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
                .andExpect(jsonPath("$.resultCode", is("200")))
                .andExpect(jsonPath("$.msg", is(Message.Success.DELETE_POST_SUCCESS.getMessage().formatted(1))))
                .andExpect(jsonPath("$.data.id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.createDate", matchesPattern(DATE_PATTERN)))
                .andExpect(jsonPath("$.data.modifyDate", matchesPattern(DATE_PATTERN)))
                .andExpect(jsonPath("$.data.authorId", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.authorName", notNullValue()))
                .andExpect(jsonPath("$.data.title", notNullValue()))
                .andExpect(jsonPath("$.data.body", notNullValue()))
                .andExpect(jsonPath("$.data.published", instanceOf(Boolean.class)))
                .andExpect(jsonPath("$.data.paid", instanceOf(Boolean.class)));

        Post post = postService.findById(1L).orElse(null);
        assertThat(post).isNull();
    }

    @Test
    @DisplayName("DELETE /api/v1/posts/1 - 200 (admin 로그인)")
    @WithUserDetails("admin")
    void t3_2() throws Exception {
        //When
        ResultActions resultActions = mvc
                .perform(delete("/api/v1/posts/1"))
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1PostsController.class))
                .andExpect(handler().methodName("deletePost"))
                .andExpect(jsonPath("$.resultCode", is("200")))
                .andExpect(jsonPath("$.msg", is(Message.Success.DELETE_POST_SUCCESS.getMessage().formatted(1))))
                .andExpect(jsonPath("$.data.id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.createDate", matchesPattern(DATE_PATTERN)))
                .andExpect(jsonPath("$.data.modifyDate", matchesPattern(DATE_PATTERN)))
                .andExpect(jsonPath("$.data.authorId", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.authorName", notNullValue()))
                .andExpect(jsonPath("$.data.title", notNullValue()))
                .andExpect(jsonPath("$.data.body", notNullValue()))
                .andExpect(jsonPath("$.data.published", instanceOf(Boolean.class)))
                .andExpect(jsonPath("$.data.paid", instanceOf(Boolean.class)));

        Post post = postService.findById(1L).orElse(null);
        assertThat(post).isNull();
    }

    @Test
    @DisplayName("DELETE /api/v1/posts/1 - 401 (비로그인)")
    void t3_3() throws Exception {
        //When
        ResultActions resultActions = mvc
                .perform(delete("/api/v1/posts/1"))
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ApiV1PostsController.class))
                .andExpect(handler().methodName("deletePost"))
                .andExpect(jsonPath("$.resultCode", is("401")))
                .andExpect(jsonPath("$.msg", is(Message.Error.NOT_LOGGED_IN.getMessage())));
    }

    @Test
    @DisplayName("DELETE /api/v1/posts/1 - 403 (로그인)")
    @WithUserDetails("user2")
    void t3_4() throws Exception {
        //When
        ResultActions resultActions = mvc
                .perform(delete("/api/v1/posts/1"))
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ApiV1PostsController.class))
                .andExpect(handler().methodName("deletePost"))
                .andExpect(jsonPath("$.resultCode", is("403")))
                .andExpect(jsonPath("$.msg", is(Message.Error.NO_DELETE_PERMISSION.getMessage())));
    }

    @Test
    @DisplayName("PUT /api/v1/posts/1 - 200 (로그인)")
    @WithUserDetails("user1")
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
                .andExpect(jsonPath("$.resultCode", is("200")))
                .andExpect(jsonPath("$.msg", is(Message.Success.MODIFY_POST_SUCCESS.getMessage().formatted(1))))
                .andExpect(jsonPath("$.data.id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.createDate", matchesPattern(DATE_PATTERN)))
                .andExpect(jsonPath("$.data.modifyDate", matchesPattern(DATE_PATTERN)))
                .andExpect(jsonPath("$.data.authorId", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.authorName", notNullValue()))
                .andExpect(jsonPath("$.data.title", is("제목1-수정")))
                .andExpect(jsonPath("$.data.body", is("내용1-수정")))
                .andExpect(jsonPath("$.data.published", is(false)))
                .andExpect(jsonPath("$.data.paid", instanceOf(Boolean.class)));
    }

    @Test
    @DisplayName("PUT /api/v1/posts/1 - 401 (비로그인)")
    void t4_2() throws Exception {
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
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ApiV1PostsController.class))
                .andExpect(handler().methodName("modifyPost"))
                .andExpect(jsonPath("$.resultCode", is("401")))
                .andExpect(jsonPath("$.msg", is(Message.Error.NOT_LOGGED_IN.getMessage())));
    }

    @Test
    @DisplayName("PUT /api/v1/posts/1 - 403 (admin 로그인)")
    @WithUserDetails("admin")
    void t4_3() throws Exception {
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
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ApiV1PostsController.class))
                .andExpect(handler().methodName("modifyPost"))
                .andExpect(jsonPath("$.resultCode", is("403")))
                .andExpect(jsonPath("$.msg", is(Message.Error.NO_MODIFY_PERMISSION.getMessage())));
    }

    @Test
    @DisplayName("POST /api/v1/posts - 200 (로그인)")
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
                .andExpect(jsonPath("$.resultCode", is("200")))
                .andExpect(jsonPath("$.msg", is(Message.Success.WRITE_POST_SUCCESS.getMessage().formatted(139))))
                .andExpect(jsonPath("$.data.id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.createDate", matchesPattern(DATE_PATTERN)))
                .andExpect(jsonPath("$.data.modifyDate", matchesPattern(DATE_PATTERN)))
                .andExpect(jsonPath("$.data.authorId", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.authorName", notNullValue()))
                .andExpect(jsonPath("$.data.title", is("제목-등록")))
                .andExpect(jsonPath("$.data.body", is("내용-등록")))
                .andExpect(jsonPath("$.data.published", is(true)))
                .andExpect(jsonPath("$.data.paid", instanceOf(Boolean.class)));
    }


    @Test
    @DisplayName("POST /api/v1/posts - 401 (비로그인)")
    void t5_2() throws Exception {
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
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ApiV1PostsController.class))
                .andExpect(handler().methodName("writePost"))
                .andExpect(jsonPath("$.resultCode", is("401")))
                .andExpect(jsonPath("$.msg", is(Message.Error.NOT_LOGGED_IN.getMessage())));
    }

    @Test
    @DisplayName("GET /api/v1/posts/latest - 200")
    void t6() throws Exception {
        //When
        ResultActions resultActions = mvc
                .perform(get("/api/v1/posts/latest"))
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1PostsController.class))
                .andExpect(handler().methodName("getLatestPosts"))
                .andExpect(jsonPath("$.resultCode", is("200")))
                .andExpect(jsonPath("$.msg", is(Message.Success.GET_LATEST_POSTS_SUCCESS.getMessage())))
                .andExpect(jsonPath("$.data[0].id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data[0].authorName", notNullValue()))
                .andExpect(jsonPath("$.data[0].title", notNullValue()))
                .andExpect(jsonPath("$.data[0].published", instanceOf(Boolean.class)))
                .andExpect(jsonPath("$.data[0].paid", instanceOf(Boolean.class)))
                .andExpect(jsonPath("$.data.length()", is(30)));
    }


    @Test
    @DisplayName("GET /api/v1/posts/1/modify - 200 (로그인)")
    @WithUserDetails("user1")
    void t7_1() throws Exception {
        //When
        ResultActions resultActions = mvc
                .perform(get("/api/v1/posts/1/modify"))
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1PostsController.class))
                .andExpect(handler().methodName("showModify"))
                .andExpect(jsonPath("$.resultCode", is("200")))
                .andExpect(jsonPath("$.msg", is(Message.Success.GET_POST_SUCCESS.getMessage().formatted(1))))
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
    @DisplayName("GET /api/v1/posts/1/modify - 403 (로그인)")
    @WithUserDetails("user2")
    void t7_2() throws Exception {
        //When
        ResultActions resultActions = mvc
                .perform(get("/api/v1/posts/1/modify"))
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ApiV1PostsController.class))
                .andExpect(handler().methodName("showModify"))
                .andExpect(jsonPath("$.resultCode", is("403")))
                .andExpect(jsonPath("$.msg", is(Message.Error.NO_MODIFY_PERMISSION.getMessage())));
    }

    @Test
    @DisplayName("GET /api/v1/posts/1000/modify - 404 (로그인)")
    @WithUserDetails("user1")
    void t7_3() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(get("/api/v1/posts/1000/modify"))
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ApiV1PostsController.class))
                .andExpect(handler().methodName("showModify"))
                .andExpect(jsonPath("$.resultCode", is("404")))
                .andExpect(jsonPath("$.msg", is(Message.Error.POST_NOT_FOUND.getMessage())));
    }

    @Test
    @DisplayName("GET /api/v1/posts/1/modify - 401 (비로그인)")
    void t7_4() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(get("/api/v1/posts/1/modify"))
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ApiV1PostsController.class))
                .andExpect(handler().methodName("showModify"))
                .andExpect(jsonPath("$.resultCode", is("401")))
                .andExpect(jsonPath("$.msg", is(Message.Error.NOT_LOGGED_IN.getMessage())));
    }

    @Test
    @DisplayName("GET /api/v1/posts/2/postPermission - 200 공개유료글 (비로그인)")
    void t8_1() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(get("/api/v1/posts/2/postPermission"))
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1PostsController.class))
                .andExpect(handler().methodName("getPostPermission"))
                .andExpect(jsonPath("$.resultCode", is("200")))
                .andExpect(jsonPath("$.msg", is(Message.Success.GET_CONTROL_PERMISSION_SUCCESS.getMessage().formatted(2))))
                .andExpect(jsonPath("$.data.canAccessContent", is(false)))
                .andExpect(jsonPath("$.data.canModify", is(false)))
                .andExpect(jsonPath("$.data.canDelete", is(false)));
    }

    @Test
    @DisplayName("GET /api/v1/posts/2/postPermission - 200 공개유료글 (로그인)")
    @WithUserDetails("user1")
    void t8_2() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(get("/api/v1/posts/2/postPermission"))
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1PostsController.class))
                .andExpect(handler().methodName("getPostPermission"))
                .andExpect(jsonPath("$.resultCode", is("200")))
                .andExpect(jsonPath("$.msg", is(Message.Success.GET_CONTROL_PERMISSION_SUCCESS.getMessage().formatted(2))))
                .andExpect(jsonPath("$.data.canAccessContent", is(true)))
                .andExpect(jsonPath("$.data.canModify", is(true)))
                .andExpect(jsonPath("$.data.canDelete", is(true)));
    }

    @Test
    @DisplayName("GET /api/v1/posts/2/postPermission - 200 공개유료글 (권한 없는 로그인)")
    @WithUserDetails("user2")
    void t8_3() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(get("/api/v1/posts/2/postPermission"))
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1PostsController.class))
                .andExpect(handler().methodName("getPostPermission"))
                .andExpect(jsonPath("$.resultCode", is("200")))
                .andExpect(jsonPath("$.msg", is(Message.Success.GET_CONTROL_PERMISSION_SUCCESS.getMessage().formatted(2))))
                .andExpect(jsonPath("$.data.canAccessContent", is(false)))
                .andExpect(jsonPath("$.data.canModify", is(false)))
                .andExpect(jsonPath("$.data.canDelete", is(false)));
    }
}
