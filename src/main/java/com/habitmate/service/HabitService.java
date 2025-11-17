package com.habitmate.service;

import com.habitmate.dto.HabitRequest;
import com.habitmate.dto.HabitResponse;
import com.habitmate.exception.CustomException;
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

@Service
@RequiredArgsConstructor
public class HabitService {

    private final HabitRepository habitRepository;
    private final UserRepository userRepository;

    /**
     * 현재 로그인한 사용자 가져오기
     */
    private User getCurrentUser(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new CustomException(ErrorMessage.USER_NOT_AUTHENTICATED);
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorMessage.LOGIN_USER_NOT_FOUND));
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
            throw new CustomException(ErrorMessage.HABIT_LIST_EMPTY);
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
            throw new CustomException(ErrorMessage.NO_COMPLETED_HABITS);
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
            throw new CustomException(ErrorMessage.HABIT_NAME_REQUIRED);
        }
    }

    private Habit findHabitOrThrow(Long id, User currentUser) {
        Habit habit = habitRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorMessage.HABIT_NOT_FOUND));

        if (!habit.getUser().getId().equals(currentUser.getId())) {
            throw new CustomException(ErrorMessage.HABIT_FORBIDDEN);
        }

        return habit;
    }
}
