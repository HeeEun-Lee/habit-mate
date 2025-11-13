package com.habitmate.controller;

import com.habitmate.dto.AuthRequest;
import com.habitmate.dto.AuthResponse;
import com.habitmate.model.User;
import com.habitmate.repository.UserRepository;
import com.habitmate.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;

    // ✅ 회원가입
    @PostMapping("/sign-up")
    public String signUp(@RequestBody AuthRequest request) {
        // 이미 존재하는 이메일 확인
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // 비밀번호 암호화 후 User 생성
        User user = User.create(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getNickname()
        );

        userRepository.save(user);
        return "회원가입이 완료되었습니다.";
    }

    // ✅ 로그인
    @PostMapping("/sign-in")
    public AuthResponse signIn(@RequestBody AuthRequest request) {
        // 이메일로 사용자 조회
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        // 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 토큰 발급
        String token = jwtTokenProvider.createToken(user.getEmail());

        return new AuthResponse(token);
    }
}
