package com.habitmate.service;

import com.habitmate.config.JwtTokenProvider;
import com.habitmate.exception.CustomException;
import com.habitmate.exception.ErrorMessage;
import com.habitmate.model.User;
import com.habitmate.repository.UserRepository;
import com.habitmate.web.dto.SignInRequest;
import com.habitmate.web.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원가입
     */
    public void signUp(SignUpRequest req) {

        // 이메일 중복 체크
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new CustomException(ErrorMessage.USER_ALREADY_EXISTS);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(req.getPassword());

        // User 생성
        User user = User.create(
                req.getEmail(),
                encodedPassword,
                req.getNickname()
        );

        // 저장
        userRepository.save(user);
    }

    /**
     * 로그인 (JWT 발급)
     */
    public String signIn(SignInRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new CustomException(ErrorMessage.USER_NOT_FOUND));

        // 비밀번호 일치 확인
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorMessage.INVALID_PASSWORD);
        }

        // JWT 발급
        return jwtTokenProvider.createToken(user.getEmail());
    }
}
