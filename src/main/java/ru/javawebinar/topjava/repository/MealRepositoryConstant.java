package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class MealRepositoryConstant implements MealRepository {

    private static final List<Meal> meals = Arrays.asList(
            new Meal(0, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(2, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(3, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new Meal(4, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(5, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(6, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
    );

    @Override
    public List<Meal> getAll() {
        return meals;
    }

    @Override
    public void add(LocalDateTime dateTime, String description, int calories) {
        throw new RuntimeException("method MealRepositoryConstant.add not implemented");
    }

    @Override
    public void delete(int mealId) {
        throw new RuntimeException("method MealRepositoryConstant.delete not implemented");
    }

    @Override
    public Meal getById(int mealId) {
        throw new RuntimeException("method MealRepositoryConstant.getById not implemented");
    }

    @Override
    public void update(int id, LocalDateTime dateTime, String description, int calories) {
        throw new RuntimeException("method MealRepositoryConstant.update not implemented");
    }
}
