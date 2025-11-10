package com.habitmate.service;

import com.habitmate.model.Habit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

class HabitServiceTest {

    private HabitService habitService;

    @BeforeEach
    void setUp() {
        habitService = new HabitService();
    }

    @Test
    @DisplayName("습관을 추가하면 목록에 포함된다")
    void createHabit_addsToList() {
        habitService.createHabit("운동하기", "매일 아침 30분");
        assertThat(habitService.getAllHabits()).hasSize(1);
    }

    @Test
    @DisplayName("존재하지 않는 ID로 완료 시 예외 발생")
    void completeHabit_invalidIdThrowsException() {
        assertThatThrownBy(() -> habitService.completeHabit(999L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당 ID의 습관이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("삭제 시 존재하지 않으면 예외 발생")
    void deleteHabit_invalidIdThrowsException() {
        assertThatThrownBy(() -> habitService.deleteHabit(123L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("삭제할 습관이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("완료율 계산이 정상적으로 수행된다")
    void completionRate_calculatesCorrectly() {
        habitService.createHabit("운동하기", "매일 30분");
        Habit h2 = habitService.createHabit("공부하기", "자바 복습");
        habitService.completeHabit(h2.getId());

        assertThat(habitService.getCompletionRate()).isEqualTo(50.0);
    }
}
