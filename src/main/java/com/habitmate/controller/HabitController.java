package com.habitmate.controller;

import com.habitmate.dto.HabitRequest;
import com.habitmate.dto.HabitResponse;
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
    public HabitResponse createHabit(@RequestBody HabitRequest request) {
        return habitService.createHabit(request);
    }

    @GetMapping
    public List<HabitResponse> getAllHabits() {
        return habitService.getAllHabits();
    }

    @GetMapping("/completed")
    public List<HabitResponse> getCompletedHabits() {
        return habitService.getCompletedHabits();
    }

    @PutMapping("/{id}/complete")
    public void completeHabit(@PathVariable Long id) {
        habitService.completeHabit(id);
    }

    @DeleteMapping("/{id}")
    public void deleteHabit(@PathVariable Long id) {
        habitService.deleteHabit(id);
    }

    @GetMapping("/completion-rate")
    public double getCompletionRate() {
        return habitService.getCompletionRate();
    }
}
