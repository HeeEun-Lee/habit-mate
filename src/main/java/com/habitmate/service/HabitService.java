package com.habitmate.service;

import com.habitmate.exception.ErrorMessage;
import com.habitmate.model.Habit;
import com.habitmate.model.Habits;
import com.habitmate.repository.HabitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HabitService {
    private final HabitRepository habitRepository;

    public HabitService(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    @Transactional
    public Habit createHabit(String name, String description) {
        validateName(name);
        return habitRepository.save(new Habit(name, description));
    }

    @Transactional
    public void completeHabit(Long id) {
        Habit habit = findHabitOrThrow(id);
        habit.setCompleted(true);
        habitRepository.save(habit);
    }

    @Transactional
    public void deleteHabit(Long id) {
        if (!habitRepository.existsById(id)) {
            throw new NoSuchElementException(ErrorMessage.HABIT_DELETE_NOT_FOUND.getMessage());
        }
        habitRepository.deleteById(id);
    }

    public List<Habit> getAllHabits() {
        return loadAllHabitsOrThrow();
    }

    public List<Habit> getCompletedHabits() {
        List<Habit> allHabits = loadAllHabitsOrThrow();
        List<Habit> completed = allHabits.stream()
                .filter(Habit::isCompleted)
                .toList();

        if (completed.isEmpty()) {
            throw new NoSuchElementException(ErrorMessage.NO_COMPLETED_HABITS.getMessage());
        }
        return completed;
    }


    public double getCompletionRate() {
        List<Habit> allHabits = loadAllHabitsOrThrow();
        Habits habits = new Habits(allHabits);
        return habits.calculateCompletionRate();
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(ErrorMessage.HABIT_NAME_REQUIRED.getMessage());
        }
    }

    private List<Habit> loadAllHabitsOrThrow() {
        List<Habit> habits = habitRepository.findAll();
        if (habits.isEmpty()) {
            throw new NoSuchElementException(ErrorMessage.HABIT_LIST_EMPTY.getMessage());
        }
        return habits;
    }

    private Habit findHabitOrThrow(Long id) {
        return habitRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.HABIT_NOT_FOUND.getMessage()));
    }
}
