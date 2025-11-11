package com.habitmate.service;

import com.habitmate.exception.ErrorMessage;
import com.habitmate.model.Habit;
import com.habitmate.repository.HabitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional  // 테스트마다 롤백되도록 설정
class HabitServiceTest {

    @Autowired
    private HabitService habitService;

    @Autowired
    private HabitRepository habitRepository;

    @BeforeEach
    void setUp() {
        habitRepository.deleteAll(); // 각 테스트 전 DB 초기화
    }

    @Test
    @DisplayName("습관을 추가하면 목록에 포함된다")
    void createHabit_addsToList() {
        habitService.createHabit("운동하기", "매일 아침 30분");

        assertThat(habitService.getAllHabits())
                .hasSize(1)
                .extracting(Habit::getName)
                .containsExactly("운동하기");
    }

    @Test
    @DisplayName("존재하지 않는 ID로 완료 시 예외 발생")
    void completeHabit_invalidIdThrowsException() {
        assertThatThrownBy(() -> habitService.completeHabit(999L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage(ErrorMessage.HABIT_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("삭제 시 존재하지 않으면 예외 발생")
    void deleteHabit_invalidIdThrowsException() {
        assertThatThrownBy(() -> habitService.deleteHabit(123L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage(ErrorMessage.HABIT_DELETE_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("완료율 계산이 정상적으로 수행된다")
    void completionRate_calculatesCorrectly() {
        Habit h1 = habitService.createHabit("운동하기", "매일 30분");
        Habit h2 = habitService.createHabit("공부하기", "자바 복습");

        habitService.completeHabit(h2.getId());

        assertThat(habitService.getCompletionRate()).isEqualTo(50.0);
    }
}
