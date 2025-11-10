package com.habitmate.controller;

import com.habitmate.model.Habit;
import com.habitmate.service.HabitService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/habits")
public class HabitController {

    private final HabitService habitService;

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    @PostMapping
    public Habit createHabit(@RequestBody Habit habit) {
        return habitService.createHabit(habit.getName(), habit.getDescription());
    }

    @GetMapping
    public List<Habit> getAllHabits() {
        return habitService.getAllHabits();
    }

    @PutMapping("/{id}/complete")
    public String completeHabit(@PathVariable Long id) {
        habitService.completeHabit(id);
        return "습관 완료 처리 성공";
    }

    @DeleteMapping("/{id}")
    public String deleteHabit(@PathVariable Long id) {
        habitService.deleteHabit(id);
        return "습관 삭제 성공";
    }

    @GetMapping("/completion-rate")
    public double getCompletionRate() {
        return habitService.getCompletionRate();
    }
}
