package com.habitmate.model;

import com.habitmate.exception.ErrorMessage;
import java.util.*;
import java.util.stream.Collectors;

public class Habits {

    private final List<Habit> habits = new ArrayList<>();
    private long nextId = 1L;

    public Habit addHabit(String name, String description) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(ErrorMessage.HABIT_NAME_REQUIRED.getMessage());
        }
        Habit habit = new Habit(nextId++, name, description);
        habits.add(habit);
        return habit;
    }

    public void removeHabitById(Long id) {
        habits.removeIf(h -> h.getId().equals(id));
    }

    public Optional<Habit> findById(Long id) {
        return habits.stream()
                .filter(h -> h.getId().equals(id))
                .findFirst();
    }

    public List<Habit> getAllHabits() {
        return Collections.unmodifiableList(habits);
    }

    public List<Habit> findCompletedHabits() {
        return habits.stream()
                .filter(Habit::isCompleted)
                .collect(Collectors.toList());
    }

    public double calculateCompletionRate() {
        if (habits.isEmpty()) return 0;
        long completed = habits.stream().filter(Habit::isCompleted).count();
        return (double) completed / habits.size() * 100;
    }

    public int size() {
        return habits.size();
    }
}
