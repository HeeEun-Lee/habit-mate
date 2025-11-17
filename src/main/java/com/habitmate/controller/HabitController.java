package com.habitmate.controller;

import com.habitmate.config.CustomUserDetails;
import com.habitmate.dto.HabitRequest;
import com.habitmate.dto.HabitResponse;
import com.habitmate.service.HabitService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/habits")
@RequiredArgsConstructor
public class HabitController {

    private final HabitService habitService;

    @PostMapping
    public HabitResponse createHabit(@RequestBody HabitRequest request, @AuthenticationPrincipal CustomUserDetails user) {
        return habitService.createHabit(request, user.getId());
    }

    @GetMapping
    public List<HabitResponse> getAllHabits(@AuthenticationPrincipal CustomUserDetails user) {
        return habitService.getAllHabits(user.getId());
    }

    @GetMapping("/completed")
    public List<HabitResponse> getCompletedHabits(@AuthenticationPrincipal CustomUserDetails user) {
        return habitService.getCompletedHabits(user.getId());
    }

    @PutMapping("/{id}/complete")
    public void completeHabit(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails user) {
        habitService.completeHabit(id, user.getId());
    }

    @DeleteMapping("/{id}")
    public void deleteHabit(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails user) {
        habitService.deleteHabit(id, user.getId());
    }

    @GetMapping("/completion-rate")
    public double getCompletionRate(@AuthenticationPrincipal CustomUserDetails user) {
        return habitService.getCompletionRate(user.getId());
    }
}
