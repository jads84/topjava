package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class MealRepositoryInMemory implements MealRepository {

    private static final List<Meal> meals = new ArrayList<>();
    private static volatile int lastId;

//            new Meal(1L, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
//            new Meal(2L, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
//            new Meal(3L, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
//            new Meal(4L, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
//            new Meal(5L, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
//            new Meal(6L, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
//            new Meal(7L, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
//    );

    @Override
    public List<Meal> getAll() {
        return meals;
    }

    @Override
    public void add(LocalDateTime dateTime, String description, int calories) {
        synchronized (meals) {
            lastId++;
            Meal meal = new Meal(lastId, dateTime, description, calories);
            meals.add(meal);
        }
    }

    @Override
    public void update(int id, LocalDateTime dateTime, String description, int calories) {
        synchronized (meals) {
            Meal meal = new Meal(id, dateTime, description, calories);
            meals.set(id, meal);
        }
    }
}
