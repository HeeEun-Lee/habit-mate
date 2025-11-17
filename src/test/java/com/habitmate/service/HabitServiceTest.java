package com.habitmate.service;

import com.habitmate.dto.HabitRequest;
import com.habitmate.dto.HabitResponse;
import com.habitmate.model.Habit;
import com.habitmate.model.User;
import com.habitmate.repository.HabitRepository;
import com.habitmate.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    private User user;

    @BeforeEach
    void setUp() {
        // 테스트용 유저 생성
        user = userRepository.save(User.create("test@habit.com", "pass", "tester"));
    }

    private HabitRequest makeRequest(String name, String description) {
        HabitRequest request = new HabitRequest();
        ReflectionTestUtils.setField(request, "name", name);
        ReflectionTestUtils.setField(request, "description", description);
        return request;
    }

    @Test
    @DisplayName("습관 생성 성공")
    void createHabit_success() {
        HabitRequest request = makeRequest("운동하기", "매일 30분");

        HabitResponse response = habitService.createHabit(request, user.getId());

        assertThat(response.getName()).isEqualTo("운동하기");
        assertThat(habitRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("습관 전체 조회")
    void getAllHabits_success() {
        habitService.createHabit(makeRequest("운동하기", "30분"), user.getId());
        habitService.createHabit(makeRequest("공부하기", "자바"), user.getId());

        List<HabitResponse> responses = habitService.getAllHabits(user.getId());

        assertThat(responses).hasSize(2);
    }

    @Test
    @DisplayName("습관 완료 처리 성공")
    void completeHabit_success() {
        HabitResponse saved = habitService.createHabit(makeRequest("운동하기", "30분"), user.getId());

        habitService.completeHabit(saved.getId(), user.getId());

        Habit updated = habitRepository.findById(saved.getId()).orElseThrow();
        assertThat(updated.isCompleted()).isTrue();
    }

    @Test
    @DisplayName("습관 삭제 성공")
    void deleteHabit_success() {
        HabitResponse saved = habitService.createHabit(makeRequest("운동하기", "30분"), user.getId());

        habitService.deleteHabit(saved.getId(), user.getId());

        assertThat(habitRepository.count()).isZero();
    }

    @Test
    @DisplayName("완료된 습관만 조회")
    void getCompletedHabits_success() {
        HabitResponse h1 = habitService.createHabit(makeRequest("운동하기", "30분"), user.getId());
        HabitResponse h2 = habitService.createHabit(makeRequest("공부하기", "복습"), user.getId());

        habitService.completeHabit(h1.getId(), user.getId());

        List<HabitResponse> completed = habitService.getCompletedHabits(user.getId());

        assertThat(completed).hasSize(1);
        assertThat(completed.get(0).getName()).isEqualTo("운동하기");
    }

    @Test
    @DisplayName("완료율 계산 성공")
    void getCompletionRate_success() {
        HabitResponse h1 = habitService.createHabit(makeRequest("운동하기", "30분"), user.getId());
        HabitResponse h2 = habitService.createHabit(makeRequest("공부하기", "복습"), user.getId());

        habitService.completeHabit(h1.getId(), user.getId());

        double rate = habitService.getCompletionRate(user.getId());

        assertThat(rate).isEqualTo(50.0);
    }
}
