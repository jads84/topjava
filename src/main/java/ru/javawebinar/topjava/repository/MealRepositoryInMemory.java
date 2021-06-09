package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

public class MealRepositoryInMemory implements MealRepository {
    private static final Map<Integer, Meal> mealsMap = new MealRepositoryConstant().getAll().stream()
            .collect(Collectors.toMap(Meal::getId, identity()));
    private static volatile int lastId = mealsMap.size();

    @Override
    public List<Meal> getAll() {
        synchronized (mealsMap) {
            return new ArrayList<>(mealsMap.values());
        }
    }

    @Override
    public void add(LocalDateTime dateTime, String description, int calories) {
        synchronized (mealsMap) {
            Meal meal = new Meal(lastId, dateTime, description, calories);
            mealsMap.put(lastId, meal);
            lastId++;
        }
    }

    @Override
    public void update(int id, LocalDateTime dateTime, String description, int calories) {
        synchronized (mealsMap) {
            Meal meal = new Meal(id, dateTime, description, calories);
            mealsMap.replace(id, meal);
        }
    }

    @Override
    public void delete(int id) {
        synchronized (mealsMap) {
            mealsMap.remove(id);
        }
    }

    @Override
    public Meal getById(int id) {
        synchronized (mealsMap) {
            return mealsMap.get(id);
        }
    }
}
