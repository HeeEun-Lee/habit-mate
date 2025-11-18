package com.habitmate.controller;

import com.habitmate.dto.AuthRequest;
import com.habitmate.repository.UserRepository;
import com.habitmate.config.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("회원가입 성공 테스트")
    void signUpSuccess() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setNickname("테스트유저");

        mockMvc.perform(post("/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("회원가입이 완료되었습니다."));
    }

    @Test
    @DisplayName("로그인 성공 시 JWT 토큰 반환")
    void signInSuccess() throws Exception {
        // 회원 미리 등록
        String encodedPw = passwordEncoder.encode("password123");
        userRepository.save(com.habitmate.model.User.create("login@test.com", encodedPw, "로그인유저"));

        AuthRequest request = new AuthRequest();
        request.setEmail("login@test.com");
        request.setPassword("password123");

        mockMvc.perform(post("/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인 시 예외 발생")
    void signInInvalidPassword() throws Exception {
        // given
        String encodedPw = passwordEncoder.encode("password123");
        userRepository.save(com.habitmate.model.User.create("wrongpw@test.com", encodedPw, "유저"));

        AuthRequest request = new AuthRequest();
        request.setEmail("wrongpw@test.com");
        request.setPassword("wrongPassword");

        // when + then
        mockMvc.perform(post("/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_PASSWORD"))
                .andExpect(jsonPath("$.message").value("비밀번호가 일치하지 않습니다."));
    }

}
