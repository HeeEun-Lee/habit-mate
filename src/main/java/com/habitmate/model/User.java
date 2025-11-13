package com.habitmate.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이메일, 패스워드, 닉네임은 지금은 사용 X, 확장을 위해 남겨둠.
    @Column(unique = true)
    private String email;

    private String password;

    @Column(nullable = false)
    private String nickname;

    public static User create(String email, String password, String nickname) {
        return User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();
    }

    public static User createDefaultUser() {
        return User.builder()
                .email("default@habitmate.com")
                .password("none")
                .nickname("defaultUser")
                .build();
    }
}
