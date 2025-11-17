package com.habitmate.controller;

import com.habitmate.dto.HabitRequest;
import com.habitmate.dto.HabitResponse;
import com.habitmate.service.HabitService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/habits")
@RequiredArgsConstructor
public class HabitController {

    private final HabitService habitService;

    @PostMapping
    public HabitResponse createHabit(@RequestBody HabitRequest request, Authentication authentication) {
        return habitService.createHabit(request, authentication);
    }

    @GetMapping
    public List<HabitResponse> getAllHabits(Authentication authentication) {
        return habitService.getAllHabits(authentication);
    }

    @GetMapping("/completed")
    public List<HabitResponse> getCompletedHabits(Authentication authentication) {
        return habitService.getCompletedHabits(authentication);
    }

    @PutMapping("/{id}/complete")
    public void completeHabit(@PathVariable Long id, Authentication authentication) {
        habitService.completeHabit(id, authentication);
    }

    @DeleteMapping("/{id}")
    public void deleteHabit(@PathVariable Long id, Authentication authentication) {
        habitService.deleteHabit(id, authentication);
    }

    @GetMapping("/completion-rate")
    public double getCompletionRate(Authentication authentication) {
        return habitService.getCompletionRate(authentication);
    }
}
