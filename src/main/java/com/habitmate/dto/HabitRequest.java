package com.habitmate.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HabitRequest {
    private String name;
    private String description;
}
