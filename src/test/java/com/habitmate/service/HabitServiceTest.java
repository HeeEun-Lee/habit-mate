package com.habitmate.service;

import com.habitmate.dto.HabitRequest;
import com.habitmate.dto.HabitResponse;
import com.habitmate.model.Habit;
import com.habitmate.repository.HabitRepository;
import com.habitmate.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;
import com.habitmate.exception.ErrorMessage;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional  // 테스트마다 롤백되도록 설정
class HabitServiceTest {

    @Autowired
    private HabitService habitService;

    @Autowired
    private HabitRepository habitRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        habitService.initDefaultUser(); // 기본 유저 초기화
    }

    private HabitRequest makeRequest(String name, String description) {
        HabitRequest request = new HabitRequest();
        ReflectionTestUtils.setField(request, "name", name);
        ReflectionTestUtils.setField(request, "description", description);
        return request;
    }


    @Test
    @DisplayName("습관을 정상적으로 생성할 수 있다")
    void createHabit_success() {
        // given
        HabitRequest request = makeRequest("운동하기", "매일 아침 30분");

        // when
        HabitResponse response = habitService.createHabit(request);

        // then
        assertThat(response.getName()).isEqualTo("운동하기");
        assertThat(response.getDescription()).isEqualTo("매일 아침 30분");
        assertThat(response.isCompleted()).isFalse();
        assertThat(habitRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("빈 이름으로 습관 생성 시 예외 발생")
    void createHabit_invalidName() {
        // given
        HabitRequest request = makeRequest(" ", "이름 없음");

        // when & then
        assertThatThrownBy(() -> habitService.createHabit(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorMessage.HABIT_NAME_REQUIRED.getMessage());
    }

    @Test
    @DisplayName("모든 습관 조회")
    void getAllHabits() {
        // given
        habitService.createHabit(makeRequest("운동하기", "매일 30분"));
        habitService.createHabit(makeRequest("공부하기", "자바 복습"));

        // when
        List<HabitResponse> responses = habitService.getAllHabits();

        // then
        assertThat(responses)
                .hasSize(2)
                .extracting(HabitResponse::getName)
                .containsExactlyInAnyOrder("운동하기", "공부하기");
    }

    @Test
    @DisplayName("습관 완료 처리 후 조회 시 completed = true")
    void completeHabit_success() {
        HabitResponse habit = habitService.createHabit(makeRequest("운동하기", "매일 30분"));

        habitService.completeHabit(habit.getId());

        Habit updated = habitRepository.findById(habit.getId()).orElseThrow();
        assertThat(updated.isCompleted()).isTrue();
    }

    @Test
    @DisplayName("습관 삭제 시 정상적으로 삭제된다")
    void deleteHabit_success() {
        HabitResponse habit = habitService.createHabit(makeRequest("운동하기", "매일 30분"));

        habitService.deleteHabit(habit.getId());

        assertThat(habitRepository.count()).isZero();
    }

    @Test
    @DisplayName("존재하지 않는 습관 삭제 시 예외 발생")
    void deleteHabit_notFound() {
        assertThatThrownBy(() -> habitService.deleteHabit(999L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining(ErrorMessage.HABIT_DELETE_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("존재하지 않는 ID로 완료 시 예외 발생")
    void completeHabit_invalidIdThrowsException() {
        assertThatThrownBy(() -> habitService.completeHabit(999L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage(ErrorMessage.HABIT_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("완료된 습관이 없을 경우 예외 발생")
    void getCompletedHabits_empty() {
        habitService.createHabit(makeRequest("운동하기", "매일 30분"));

        assertThatThrownBy(() -> habitService.getCompletedHabits())
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining(ErrorMessage.NO_COMPLETED_HABITS.getMessage());
    }

    @Test
    @DisplayName("완료된 습관만 필터링해서 조회 가능")
    void getCompletedHabits_success() {
        HabitResponse h1 = habitService.createHabit(makeRequest("운동하기", "매일 30분"));
        HabitResponse h2 = habitService.createHabit(makeRequest("공부하기", "자바 복습"));
        habitService.completeHabit(h1.getId());

        List<HabitResponse> completed = habitService.getCompletedHabits();

        assertThat(completed)
                .hasSize(1)
                .extracting(HabitResponse::getName)
                .containsExactly("운동하기");
    }

    @Test
    @DisplayName("습관 완료율 계산 — 전체 대비 완료된 비율 반환")
    void getCompletionRate_success() {
        // given
        HabitResponse h1 = habitService.createHabit(makeRequest("운동하기", "매일 30분"));
        HabitResponse h2 = habitService.createHabit(makeRequest("공부하기", "자바 복습"));
        HabitResponse h3 = habitService.createHabit(makeRequest("독서하기", "하루 10쪽 읽기"));

        habitService.completeHabit(h1.getId());
        habitService.completeHabit(h2.getId());
        // 총 3개 중 2개 완료 → 66.6%

        // when
        double rate = habitService.getCompletionRate();

        // then
        assertThat(rate).isCloseTo(66.7, within(0.1));
    }

    @Test
    @DisplayName("습관이 하나도 없을 때 완료율 계산 시 예외 발생")
    void getCompletionRate_empty() {
        assertThatThrownBy(() -> habitService.getCompletionRate())
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining(ErrorMessage.HABIT_LIST_EMPTY.getMessage());
    }
}
