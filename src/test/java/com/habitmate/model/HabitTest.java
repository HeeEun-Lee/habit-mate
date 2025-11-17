package com.habitmate.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class HabitTest {

    @Test
    @DisplayName("습관 생성 시 기본 완료 상태는 false이다")
    void habitDefaultCompletionIsFalse() {
        Habit habit = Habit.builder()
                .name("운동하기")
                .description("매일 30분")
                .build();
        assertThat(habit.isCompleted()).isFalse();
    }

    @Test
    @DisplayName("습관 완료 처리 후 completed는 true가 된다")
    void habitCompletionChangesState() {
        Habit habit = Habit.builder()
                .name("운동하기")
                .description("매일 30분")
                .build();
        habit.setCompleted(true);

        assertThat(habit.isCompleted()).isTrue();
    }

    @Test
    @DisplayName("습관의 이름과 설명이 올바르게 저장된다")
    void habitStoresNameAndDescription() {
        Habit habit = Habit.builder()
                .name("공부하기")
                .description("매일 1시간 자바 복습")
                .build();
        assertThat(habit.getName()).isEqualTo("공부하기");
        assertThat(habit.getDescription()).isEqualTo("매일 1시간 자바 복습");
    }
}
