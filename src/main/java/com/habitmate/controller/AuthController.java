package com.habitmate.controller;

import com.habitmate.dto.AuthResponse;
import com.habitmate.web.dto.SignInRequest;
import com.habitmate.web.dto.SignUpRequest;
import com.habitmate.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 회원가입
     */
    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpRequest request) {
        authService.signUp(request);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

    /**
     * 로그인 & JWT 발급
     */
    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponse> signIn(@Valid @RequestBody SignInRequest request) {
        String token = authService.signIn(request);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    /**
     * JWT 인증 확인용 엔드포인트
     */
    @GetMapping("/test-auth")
    public ResponseEntity<String> testAuth(Authentication authentication) {
        return ResponseEntity.ok("현재 로그인된 사용자: " + authentication.getName());
    }
}
