package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface MealRepository {
    void add(LocalDateTime dateTime, String description, int calories);

    List<Meal> getAll();

    void delete(int id);

    Meal getById(int id);

    void update(int id, LocalDateTime dateTime, String description, int calories);
}
