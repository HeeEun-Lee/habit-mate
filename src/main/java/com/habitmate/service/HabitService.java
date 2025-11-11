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
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HabitService {
    private final HabitRepository habitRepository;
    private final UserRepository userRepository;
    private User defaultUser;

    public HabitService(HabitRepository habitRepository, UserRepository userRepository) {
        this.habitRepository = habitRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void initDefaultUser() {
        if (userRepository.count() == 0) {
            userRepository.save(User.createDefaultUser());
        }
        defaultUser = userRepository.findAll().get(0);
    }

    @Transactional
    public HabitResponse createHabit(HabitRequest request) {
        validateName(request.getName());
        Habit habit = HabitMapper.toEntity(request, defaultUser);
        Habit saved = habitRepository.save(habit);
        return HabitMapper.toResponse(saved);
    }

    @Transactional
    public void completeHabit(Long id) {
        Habit habit = findHabitOrThrow(id);
        habit.setCompleted(true);
        habitRepository.save(habit);
    }

    @Transactional
    public void deleteHabit(Long id) {
        if (!habitRepository.existsById(id)) {
            throw new NoSuchElementException(ErrorMessage.HABIT_DELETE_NOT_FOUND.getMessage());
        }
        habitRepository.deleteById(id);
    }

    public List<HabitResponse> getAllHabits() {
        return loadAllHabitsOrThrow()
                .stream()
                .map(HabitMapper::toResponse)
                .toList();
    }

    public List<HabitResponse> getCompletedHabits() {
        List<Habit> allHabits = loadAllHabitsOrThrow();
        List<Habit> completed = allHabits.stream()
                .filter(Habit::isCompleted)
                .toList();

        if (completed.isEmpty()) {
            throw new NoSuchElementException(ErrorMessage.NO_COMPLETED_HABITS.getMessage());
        }
        return completed.stream()
                .map(HabitMapper::toResponse)
                .toList();
    }


    public double getCompletionRate() {
        List<Habit> allHabits = loadAllHabitsOrThrow();
        Habits habits = new Habits(allHabits);
        return habits.calculateCompletionRate();
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(ErrorMessage.HABIT_NAME_REQUIRED.getMessage());
        }
    }

    private List<Habit> loadAllHabitsOrThrow() {
        List<Habit> habits = habitRepository.findAll();
        if (habits.isEmpty()) {
            throw new NoSuchElementException(ErrorMessage.HABIT_LIST_EMPTY.getMessage());
        }
        return habits;
    }

    private Habit findHabitOrThrow(Long id) {
        Habit habit = habitRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.HABIT_NOT_FOUND.getMessage()));

        if (!habit.getUser().getId().equals(defaultUser.getId())) {
            throw new IllegalArgumentException("다른 사용자의 습관에는 접근할 수 없습니다.");
        }

        return habit;
    }
}
