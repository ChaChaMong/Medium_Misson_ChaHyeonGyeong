package com.ll.medium.domain.member.member.controller;

import com.ll.medium.global.common.ErrorMessage;
import com.ll.medium.global.common.SuccessMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ApiV1MembersControllerTest {
    @Autowired
    private MockMvc mvc;
    // 날짜 패턴 정규식
    private static final String DATE_PATTERN = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.?\\d{0,7}";

    @Test
    @DisplayName("POST /api/v1/members/login - 200")
    void t0() throws Exception {
        //When
        ResultActions resultActions = mvc
                .perform(post("/api/v1/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                        {
                                             "username" : "user1",
                                             "password" : "1234"
                                        }
                                        """)
                )
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1MembersController.class))
                .andExpect(handler().methodName("login"))
                .andExpect(jsonPath("$.resultCode", is("200")))
                .andExpect(jsonPath("$.msg", is(SuccessMessage.LOGIN_SUCCESS.getMessage())))
                .andExpect(jsonPath("$.data.item.id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.item.createDate", matchesPattern(DATE_PATTERN)))
                .andExpect(jsonPath("$.data.item.modifyDate", matchesPattern(DATE_PATTERN)))
                .andExpect(jsonPath("$.data.item.username", notNullValue()))
                .andExpect(jsonPath("$.data.accessToken", notNullValue()));
    }

    @Test
    @DisplayName("POST /api/v1/members/login - 404 (아이디 없음)")
    void t0_2() throws Exception {
        //When
        ResultActions resultActions = mvc
                .perform(post("/api/v1/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                        {
                                             "username" : "user0",
                                             "password" : "1234"
                                        }
                                        """)
                )
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ApiV1MembersController.class))
                .andExpect(handler().methodName("login"))
                .andExpect(jsonPath("$.resultCode", is("404")))
                .andExpect(jsonPath("$.msg", is(ErrorMessage.LOGIN_FAIL.getMessage())));
    }


    @Test
    @DisplayName("POST /api/v1/members/login - 404 (비밀번호 틀림)")
    void t0_3() throws Exception {
        //When
        ResultActions resultActions = mvc
                .perform(post("/api/v1/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                        {
                                             "username" : "user1",
                                             "password" : "12345"
                                        }
                                        """)
                )
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ApiV1MembersController.class))
                .andExpect(handler().methodName("login"))
                .andExpect(jsonPath("$.resultCode", is("404")))
                .andExpect(jsonPath("$.msg", is(ErrorMessage.LOGIN_FAIL.getMessage())));
    }

    @Test
    @DisplayName("POST /api/v1/members/join - 200")
    void t1() throws Exception {
        //When
        ResultActions resultActions = mvc
                .perform(post("/api/v1/members/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                        {
                                             "username" : "new1",
                                             "password" : "1234",
                                             "passwordConfirm" : "1234"
                                        }
                                        """)
                )
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1MembersController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(jsonPath("$.resultCode", is("200")))
                .andExpect(jsonPath("$.msg", is(SuccessMessage.JOIN_SUCCESS.getMessage())))
                .andExpect(jsonPath("$.data.id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.createDate", matchesPattern(DATE_PATTERN)))
                .andExpect(jsonPath("$.data.modifyDate", matchesPattern(DATE_PATTERN)))
                .andExpect(jsonPath("$.data.username", notNullValue()));
    }

    @Test
    @DisplayName("POST /api/v1/members/join - 404 (비밀번호 확인 틀림)")
    void t1_2() throws Exception {
        //When
        ResultActions resultActions = mvc
                .perform(post("/api/v1/members/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                        {
                                             "username" : "new1",
                                             "password" : "1234",
                                             "passwordConfirm" : "12345"
                                        }
                                        """)
                )
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ApiV1MembersController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(jsonPath("$.resultCode", is("404")))
                .andExpect(jsonPath("$.msg", is(ErrorMessage.NOT_MATCH_PASSWORD.getMessage())));
    }

    @Test
    @DisplayName("POST /api/v1/members/join - 404 (아이디 이미 존재)")
    void t1_3() throws Exception {
        //When
        ResultActions resultActions = mvc
                .perform(post("/api/v1/members/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                        {
                                             "username" : "user1",
                                             "password" : "1234",
                                             "passwordConfirm" : "1234"
                                        }
                                        """)
                )
                .andDo(print());

        //Then
        resultActions
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ApiV1MembersController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(jsonPath("$.resultCode", is("404")))
                .andExpect(jsonPath("$.msg", is(ErrorMessage.EXIST_USERNAME.getMessage())));
    }
}
