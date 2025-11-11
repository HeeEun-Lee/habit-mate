package com.habitmate.repository;

import com.habitmate.model.Habit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class HabitRepositoryTest {

    @Autowired
    private HabitRepository habitRepository;

    @Test
    @DisplayName("습관을 저장하고 조회할 수 있다")
    void saveAndFindHabit() {
        // given
        Habit habit = new Habit("운동하기", "매일 30분");

        // when
        Habit savedHabit = habitRepository.save(habit);
        Optional<Habit> foundHabit = habitRepository.findById(savedHabit.getId());

        // then
        assertThat(foundHabit).isPresent();
        assertThat(foundHabit.get().getName()).isEqualTo("운동하기");
        assertThat(foundHabit.get().getDescription()).isEqualTo("매일 30분");
        assertThat(foundHabit.get().isCompleted()).isFalse();
    }

    @Test
    @DisplayName("습관 목록을 조회할 수 있다")
    void findAllHabits() {
        habitRepository.save(new Habit("운동하기", "매일 30분"));
        habitRepository.save(new Habit("독서하기", "하루 한 챕터"));

        List<Habit> habits = habitRepository.findAll();

        assertThat(habits).hasSize(2);
        assertThat(habits)
                .extracting(Habit::getName)
                .containsExactlyInAnyOrder("운동하기", "독서하기");
    }

    @Test
    @DisplayName("습관을 삭제할 수 있다")
    void deleteHabit() {
        Habit habit = habitRepository.save(new Habit("명상하기", "10분간 집중"));

        habitRepository.deleteById(habit.getId());

        Optional<Habit> deleted = habitRepository.findById(habit.getId());
        assertThat(deleted).isEmpty();
    }
}
