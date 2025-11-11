package com.habitmate.model;

import com.habitmate.exception.ErrorMessage;
import java.util.*;

public class Habits {

    private final List<Habit> values;

    public Habits(List<Habit> values) {
        this.values = values;
    }

    public double calculateCompletionRate() {
        if (values == null || values.isEmpty()) {
            throw new NoSuchElementException(ErrorMessage.NO_COMPLETED_HABITS.getMessage());
        }
        long completed = values.stream().filter(Habit::isCompleted).count();
        double rate = ((double) completed / values.size()) * 100.0;

        // 소수점 한 자리까지 반올림
        return Math.round(rate * 10.0) / 10.0;
    }

    public List<Habit> getCompletedHabits() {
        return values.stream().filter(Habit::isCompleted).toList();
    }

    public List<Habit> getAll() {
        return List.copyOf(values);
    }
}
