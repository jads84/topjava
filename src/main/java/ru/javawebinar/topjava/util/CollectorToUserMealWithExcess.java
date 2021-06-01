package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.TimeUtil.isBetweenHalfOpen;
import static ru.javawebinar.topjava.util.UserMealsUtil.createUserMealWithExcess;

public class CollectorToUserMealWithExcess implements Collector<UserMeal, List<UserMeal>, List<UserMealWithExcess>> {

    private final LocalTime startTime;
    private final LocalTime endTime;
    private final int caloriesLimit;

    public CollectorToUserMealWithExcess(LocalTime startTime, LocalTime endTime, int caloriesLimit) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.caloriesLimit = caloriesLimit;
    }

    @Override
    public Supplier<List<UserMeal>> supplier() {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<UserMeal>, UserMeal> accumulator() {
        return List::add;
    }

    @Override
    public BinaryOperator<List<UserMeal>> combiner() {
        return (l, r) -> {
            l.addAll(r);
            return l;
        };
    }

    @Override
    public Function<List<UserMeal>, List<UserMealWithExcess>> finisher() {
        Map<LocalDate, Integer> caloriesPerDay = new HashMap<>();
        return userMeals -> userMeals.stream()
                .peek(meal -> caloriesPerDay.merge(meal.getLocalDate(), meal.getCalories(), Integer::sum))
                .filter(meal -> isBetweenHalfOpen(meal.getLocalTime(), startTime, endTime))
                .map(meal -> createUserMealWithExcess(meal, () -> caloriesPerDay.get(meal.getLocalDate()) > caloriesLimit))
                .collect(Collectors.toList());
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.CONCURRENT);
    }
}
