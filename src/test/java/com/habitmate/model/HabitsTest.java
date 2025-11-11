package com.habitmate.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class HabitsTest {

    @Test
    @DisplayName("완료율 계산이 정상적으로 된다")
    void completionRateWorks() {
        Habits habits = new Habits();
        Habit h1 = habits.addHabit("운동하기", "매일 30분");
        Habit h2 = habits.addHabit("독서하기", "하루 한 챕터");
        h2.setCompleted(true);

        assertThat(habits.calculateCompletionRate()).isEqualTo(50.0);
    }

    @Test
    @DisplayName("수정 불가능한 리스트 반환")
    void getAllIsUnmodifiable() {
        Habits habits = new Habits();
        habits.addHabit("공부하기", "매일 1시간");

        assertThatThrownBy(() -> habits.getAllHabits().add(new Habit(2L, "산책하기", "10분")))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
