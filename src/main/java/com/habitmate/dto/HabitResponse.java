package com.habitmate.dto;

import com.habitmate.model.Habit;
import lombok.Getter;

@Getter
public class HabitResponse {
    private final Long id;
    private final String name;
    private final String description;
    private final boolean completed;
    private final String userNickname;

    public HabitResponse(Habit habit) {
        this.id = habit.getId();
        this.name = habit.getName();
        this.description = habit.getDescription();
        this.completed = habit.isCompleted();
        this.userNickname = habit.getUser() != null ? habit.getUser().getNickname() : null;
    }
}
