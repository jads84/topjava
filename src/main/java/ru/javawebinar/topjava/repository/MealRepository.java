package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface MealRepository {
    default void add(LocalDateTime dateTime, String description, int calories) {
        throw new RuntimeException("method MealRepository.add not implemented");
    }

    List<Meal> getAll();

    default void delete(long mealId) {
        throw new RuntimeException("method MealRepository.delete not implemented");
    }

    default Object getById(long mealId) {
        throw new RuntimeException("method MealRepository.getById not implemented");
    }

    default void update(int id, LocalDateTime dateTime, String description, int calories) {
        throw new RuntimeException("method MealRepository.update not implemented");
    }
}
