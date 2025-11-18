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
    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorMessage.LOGIN_USER_NOT_FOUND));
    }

    @Transactional
    public HabitResponse createHabit(HabitRequest request, Long userId) {
        validateName(request.getName());

        User currentUser = getUserOrThrow(userId);

        Habit habit = HabitMapper.toEntity(request, currentUser);
        Habit saved = habitRepository.save(habit);

        return HabitMapper.toResponse(saved);
    }

    @Transactional
    public void completeHabit(Long id, Long userId) {
        User currentUser = getUserOrThrow(userId);

        Habit habit = findHabitOrThrow(id, userId);

        habit.setCompleted(true);
        habitRepository.save(habit);
    }

    @Transactional
    public void deleteHabit(Long id, Long userId) {
        Habit habit = findHabitOrThrow(id, userId);
        habitRepository.delete(habit);
    }

    public List<HabitResponse> getAllHabits(Long userId) {
        User currentUser = getUserOrThrow(userId);

        List<Habit> habits = habitRepository.findByUser(currentUser);

        if (habits.isEmpty()) {
            throw new CustomException(ErrorMessage.HABIT_LIST_EMPTY);
        }

        return habits.stream()
                .map(HabitMapper::toResponse)
                .toList();
    }

    public List<HabitResponse> getCompletedHabits(Long userId) {
        User currentUser = getUserOrThrow(userId);

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

    public double getCompletionRate(Long userId) {
        User currentUser = getUserOrThrow(userId);

        List<Habit> habits = habitRepository.findByUser(currentUser);

        Habits wrapper = new Habits(habits);
        return wrapper.calculateCompletionRate();
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new CustomException(ErrorMessage.HABIT_NAME_REQUIRED);
        }
    }

    private Habit findHabitOrThrow(Long id, Long userId) {
        Habit habit = habitRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorMessage.HABIT_NOT_FOUND));

        if (!habit.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorMessage.HABIT_FORBIDDEN);
        }

        return habit;
    }
}
