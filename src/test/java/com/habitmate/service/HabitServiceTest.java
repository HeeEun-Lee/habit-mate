package com.habitmate.service;

import com.habitmate.dto.HabitRequest;
import com.habitmate.dto.HabitResponse;
import com.habitmate.model.Habit;
import com.habitmate.model.User;
import com.habitmate.repository.HabitRepository;
import com.habitmate.repository.UserRepository;
import com.habitmate.service.HabitService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class HabitServiceTest {

    @Autowired
    private HabitService habitService;

    @Autowired
    private HabitRepository habitRepository;

    @Autowired
    private UserRepository userRepository;

    private Authentication auth;

    @BeforeEach
    void setUp() {
        // 테스트용 유저 생성
        User user = User.create("test@habit.com", "pass", "tester");
        userRepository.save(user);

        // Authentication 객체 생성
        auth = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    private HabitRequest makeRequest(String name, String description) {
        HabitRequest request = new HabitRequest();
        ReflectionTestUtils.setField(request, "name", name);
        ReflectionTestUtils.setField(request, "description", description);
        return request;
    }

    @Test
    @DisplayName("습관 생성")
    void createHabit_success() {
        HabitRequest request = makeRequest("운동하기", "매일 30분");

        HabitResponse response = habitService.createHabit(request, auth);

        assertThat(response.getName()).isEqualTo("운동하기");
        assertThat(habitRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("습관 전체 조회")
    void getAllHabits() {
        habitService.createHabit(makeRequest("운동하기", "30분"), auth);
        habitService.createHabit(makeRequest("공부하기", "자바"), auth);

        List<HabitResponse> responses = habitService.getAllHabits(auth);

        assertThat(responses).hasSize(2);
    }

    @Test
    @DisplayName("습관 완료 처리")
    void completeHabit_success() {
        HabitResponse habit = habitService.createHabit(makeRequest("운동하기", "30분"), auth);

        habitService.completeHabit(habit.getId(), auth);

        Habit updated = habitRepository.findById(habit.getId()).orElseThrow();
        assertThat(updated.isCompleted()).isTrue();
    }

    @Test
    @DisplayName("습관 삭제")
    void deleteHabit_success() {
        HabitResponse habit = habitService.createHabit(makeRequest("운동하기", "30분"), auth);

        habitService.deleteHabit(habit.getId(), auth);

        assertThat(habitRepository.count()).isZero();
    }

    @Test
    @DisplayName("완료된 습관 필터링")
    void getCompletedHabits_success() {
        HabitResponse h1 = habitService.createHabit(makeRequest("운동하기", "30분"), auth);
        HabitResponse h2 = habitService.createHabit(makeRequest("공부하기", "복습"), auth);

        habitService.completeHabit(h1.getId(), auth);

        List<HabitResponse> completed = habitService.getCompletedHabits(auth);

        assertThat(completed).hasSize(1);
        assertThat(completed.get(0).getName()).isEqualTo("운동하기");
    }

    @Test
    @DisplayName("완료율 계산")
    void getCompletionRate_success() {
        HabitResponse h1 = habitService.createHabit(makeRequest("운동하기", "30분"), auth);
        HabitResponse h2 = habitService.createHabit(makeRequest("공부하기", "복습"), auth);

        habitService.completeHabit(h1.getId(), auth);

        double rate = habitService.getCompletionRate(auth);

        assertThat(rate).isEqualTo(50.0);
    }
}
