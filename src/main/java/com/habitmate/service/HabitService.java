package com.habitmate.service;

import com.habitmate.dto.HabitRequest;
import com.habitmate.dto.HabitResponse;
import com.habitmate.exception.ErrorMessage;
import com.habitmate.mapper.HabitMapper;
import com.habitmate.model.Habit;
import com.habitmate.model.Habits;
import com.habitmate.model.User;
import com.habitmate.repository.HabitRepository;
import com.habitmate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class HabitService {

    private final HabitRepository habitRepository;
    private final UserRepository userRepository;

    /**
     * 현재 로그인한 사용자 가져오기
     */
    private User getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("로그인한 사용자를 찾을 수 없습니다."));
    }

    @Transactional
    public HabitResponse createHabit(HabitRequest request, Authentication authentication) {
        validateName(request.getName());

        User currentUser = getCurrentUser(authentication);

        Habit habit = HabitMapper.toEntity(request, currentUser);
        Habit saved = habitRepository.save(habit);

        return HabitMapper.toResponse(saved);
    }

    @Transactional
    public void completeHabit(Long id, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);

        Habit habit = findHabitOrThrow(id, currentUser);

        habit.setCompleted(true);
        habitRepository.save(habit);
    }

    @Transactional
    public void deleteHabit(Long id, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);

        Habit habit = findHabitOrThrow(id, currentUser);

        habitRepository.delete(habit);
    }

    public List<HabitResponse> getAllHabits(Authentication authentication) {
        User currentUser = getCurrentUser(authentication);

        List<Habit> habits = habitRepository.findByUser(currentUser);

        if (habits.isEmpty()) {
            throw new NoSuchElementException(ErrorMessage.HABIT_LIST_EMPTY.getMessage());
        }

        return habits.stream()
                .map(HabitMapper::toResponse)
                .toList();
    }

    public List<HabitResponse> getCompletedHabits(Authentication authentication) {
        User currentUser = getCurrentUser(authentication);

        List<Habit> habits = habitRepository.findByUser(currentUser);
        List<Habit> completed = habits.stream()
                .filter(Habit::isCompleted)
                .toList();

        if (completed.isEmpty()) {
            throw new NoSuchElementException(ErrorMessage.NO_COMPLETED_HABITS.getMessage());
        }

        return completed.stream()
                .map(HabitMapper::toResponse)
                .toList();
    }

    public double getCompletionRate(Authentication authentication) {
        User currentUser = getCurrentUser(authentication);

        List<Habit> habits = habitRepository.findByUser(currentUser);

        Habits wrapper = new Habits(habits);
        return wrapper.calculateCompletionRate();
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(ErrorMessage.HABIT_NAME_REQUIRED.getMessage());
        }
    }

    private Habit findHabitOrThrow(Long id, User currentUser) {
        Habit habit = habitRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.HABIT_NOT_FOUND.getMessage()));

        if (!habit.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("본인의 습관만 조회할 수 있습니다.");
        }

        return habit;
    }
}
