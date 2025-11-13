package com.habitmate.integration;

import com.habitmate.model.Habit;
import com.habitmate.model.User;
import com.habitmate.repository.HabitRepository;
import com.habitmate.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserHabitIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HabitRepository habitRepository;

    @Test
    @DisplayName("User와 Habit이 실제 DB에서 연관관계로 매핑된다")
    void userHabitMappingTest() {
        // given
        User user = User.builder()
                .email("test@habitmate.com")
                .password("encodedPw")
                .nickname("테스트유저")
                .build();

        userRepository.save(user);

        Habit habit = Habit.builder()
                .name("운동하기")
                .description("매일 30분")
                .user(user) // 연관관계 설정
                .build();

        habitRepository.save(habit);

        // when
        Habit foundHabit = habitRepository.findById(habit.getId()).orElseThrow();
        User foundUser = foundHabit.getUser();

        // then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo("test@habitmate.com");
        assertThat(foundUser.getNickname()).isEqualTo("테스트유저");
    }
}
