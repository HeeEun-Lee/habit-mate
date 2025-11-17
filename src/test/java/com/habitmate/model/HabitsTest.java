package com.habitmate.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class HabitsTest {

    private Habit createHabit(String name, String description, boolean completed) {
        return Habit.builder()
                .name(name)
                .description(description)
                .completed(completed)
                .build();
    }

    @Test
    @DisplayName("완료율 계산이 정상적으로 된다")
    void completionRateWorks() {
        Habit h1 = createHabit("운동하기", "매일 30분", false);
        Habit h2 = createHabit("독서하기", "하루 한 챕터", true);
        h2.setCompleted(true);

        Habits habits = new Habits(List.of(h1, h2));

        assertThat(habits.calculateCompletionRate()).isEqualTo(50.0);
    }

    @Test
    @DisplayName("수정 불가능한 리스트 반환")
    void getAllIsUnmodifiable() {
        Habit h1 = createHabit("운동하기", "매일 30분", false);
        Habits habits = new Habits(List.of(h1));

        assertThatThrownBy(() ->
                habits.getAll().add(createHabit("산책하기", "10분", false))
        ).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("완료된 습관만 필터링된다")
    void getCompletedHabitsReturnsOnlyCompleted() {
        Habit h1 = createHabit("운동하기", "매일 30분", false);
        Habit h2 = createHabit("독서하기", "하루 한 챕터", true);
        h2.setCompleted(true);

        Habits habits = new Habits(List.of(h1, h2));

        assertThat(habits.getCompletedHabits())
                .hasSize(1)
                .extracting(Habit::getName)
                .containsExactly("독서하기");
    }
}
