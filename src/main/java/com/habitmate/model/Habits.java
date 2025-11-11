package com.habitmate.model;

import java.util.*;

public class Habits {

    private final List<Habit> values;

    public Habits(List<Habit> values) {
        this.values = values;
    }

    public double calculateCompletionRate() {
        if (values.isEmpty()) return 0;
        long completed = values.stream().filter(Habit::isCompleted).count();
        return (double) completed / values.size() * 100;
    }

    public List<Habit> getCompletedHabits() {
        return values.stream().filter(Habit::isCompleted).toList();
    }

    public List<Habit> getAll() {
        return List.copyOf(values);
    }
}
