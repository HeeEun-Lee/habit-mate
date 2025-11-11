package com.habitmate.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class HabitsTest {

    @Test
    @DisplayName("완료율 계산이 정상적으로 된다")
    void completionRateWorks() {
        Habit h1 = new Habit("운동하기", "매일 30분");
        Habit h2 = new Habit("독서하기", "하루 한 챕터");
        h2.setCompleted(true);

        Habits habits = new Habits(List.of(h1, h2));

        assertThat(habits.calculateCompletionRate()).isEqualTo(50.0);
    }

    @Test
    @DisplayName("수정 불가능한 리스트 반환")
    void getAllIsUnmodifiable() {
        Habit h1 = new Habit("공부하기", "매일 1시간");
        Habits habits = new Habits(List.of(h1));

        assertThatThrownBy(() ->
                habits.getAll().add(new Habit("산책하기", "10분"))
        ).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("완료된 습관만 필터링된다")
    void getCompletedHabitsReturnsOnlyCompleted() {
        Habit h1 = new Habit("운동하기", "매일 30분");
        Habit h2 = new Habit("독서하기", "하루 한 챕터");
        h2.setCompleted(true);

        Habits habits = new Habits(List.of(h1, h2));

        assertThat(habits.getCompletedHabits())
                .hasSize(1)
                .extracting(Habit::getName)
                .containsExactly("독서하기");
    }
}
