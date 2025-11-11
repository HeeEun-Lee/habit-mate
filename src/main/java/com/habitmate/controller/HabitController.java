package com.habitmate.controller;

import com.habitmate.model.Habit;
import com.habitmate.service.HabitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Habit> createHabit(@RequestBody Habit habit) {
        Habit saved = habitService.createHabit(habit.getName(), habit.getDescription());
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Habit>> getAllHabits() {
        return ResponseEntity.ok(habitService.getAllHabits());
    }

    @GetMapping("/completed")
    public ResponseEntity<List<Habit>> getCompletedHabits() {
        return ResponseEntity.ok(habitService.getCompletedHabits());
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Void> completeHabit(@PathVariable Long id) {
        habitService.completeHabit(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHabit(@PathVariable Long id) {
        habitService.deleteHabit(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/completion-rate")
    public ResponseEntity<Double> getCompletionRate() {
        return ResponseEntity.ok(habitService.getCompletionRate());
    }
}
