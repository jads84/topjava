package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealRepository {
    List<Meal> getAll();

    default void delete(long mealId) {
    }

    default Object getMealById(long mealId) {
        return null;
    }
}
