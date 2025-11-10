package com.habitmate.service;

import static com.habitmate.exception.ErrorMessage.HABIT_NAME_REQUIRED;

import com.habitmate.model.Habit;
import com.habitmate.model.Habits;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
@Service
public class HabitService {
    private final Habits habits = new Habits();

    public Habit createHabit(String name, String description) {
        return habits.add(name, description);
    }

    public List<Habit> getAllHabits() {
        return habits.getAll();
    }

    public void completeHabit(Long id) {
        Habit habit = habits.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 습관이 존재하지 않습니다."));
        habit.setCompleted(true);
    }

    public void deleteHabit(Long id) {
        if (habits.findById(id).isEmpty()) {
            throw new NoSuchElementException("삭제할 습관이 존재하지 않습니다.");
        }
        habits.remove(id);
    }

    public double getCompletionRate() {
        return habits.getCompletionRate();
    }
}
