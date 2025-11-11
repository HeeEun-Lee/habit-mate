package com.habitmate.service;

import com.habitmate.exception.ErrorMessage;
import com.habitmate.model.Habit;
import com.habitmate.model.Habits;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
@Service
public class HabitService {
    private final Habits habits = new Habits();

    public Habit createHabit(String name, String description) {
        return habits.addHabit(name, description);
    }

    public List<Habit> getAllHabits() {
        return habits.getAllHabits();
    }

    public void completeHabit(Long id) {
        Habit habit = habits.findById(id)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.HABIT_NOT_FOUND.getMessage()));
        habit.setCompleted(true);
    }

    public void deleteHabit(Long id) {
        if (habits.findById(id).isEmpty()) {
            throw new NoSuchElementException(ErrorMessage.HABIT_DELETE_NOT_FOUND.getMessage());
        }
        habits.removeHabitById(id);
    }

    public double getCompletionRate() {
        return habits.calculateCompletionRate();
    }
}
