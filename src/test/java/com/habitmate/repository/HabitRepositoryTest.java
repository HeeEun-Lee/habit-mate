package com.habitmate.repository;

import com.habitmate.model.Habit;
import com.habitmate.model.User;
import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.create("test@habit.com", "pw", "tester");
        userRepository.save(user);
    }

    @Test
    @DisplayName("습관을 저장하고 조회할 수 있다")
    void saveAndFindHabit() {
        // given
        Habit habit = Habit.builder()
                .name("운동하기")
                .description("매일 30분")
                .user(user)
                .build();

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
        habitRepository.save(Habit.builder().name("운동하기").description("30분").user(user).build());
        habitRepository.save(Habit.builder().name("독서하기").description("한 챕터").user(user).build());

        List<Habit> habits = habitRepository.findAll();

        assertThat(habits).hasSize(2);
        assertThat(habits)
                .extracting(Habit::getName)
                .containsExactlyInAnyOrder("운동하기", "독서하기");
    }

    @Test
    @DisplayName("습관을 삭제할 수 있다")
    void deleteHabit() {
        Habit habit = habitRepository.save(
                Habit.builder().name("명상하기").description("10분").user(user).build()
        );

        habitRepository.deleteById(habit.getId());

        Optional<Habit> deleted = habitRepository.findById(habit.getId());
        assertThat(deleted).isEmpty();
    }
}
