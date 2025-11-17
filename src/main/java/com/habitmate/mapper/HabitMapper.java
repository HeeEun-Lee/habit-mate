package com.habitmate.mapper;

import com.habitmate.dto.HabitRequest;
import com.habitmate.dto.HabitResponse;
import com.habitmate.model.Habit;
import com.habitmate.model.User;

public class HabitMapper {

    private HabitMapper() { }

    public static HabitResponse toResponse(Habit habit) {
        return HabitResponse.of(habit);
    }


    public static Habit toEntity(HabitRequest request, User user) {
        return Habit.builder()
                .name(request.getName())
                .description(request.getDescription())
                .user(user)
                .completed(false)
                .build();
    }
}
