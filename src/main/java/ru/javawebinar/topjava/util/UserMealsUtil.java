package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));

        mealsTo = filteredByOneCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByOneStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));

        System.out.println(filteredByOneStreamsWithCollector(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static UserMealWithExcess createUserMealWithExcess(UserMeal meal, boolean excess) {
        return new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }

    public static UserMealWithExcess createUserMealWithExcess(UserMeal meal, Supplier<Boolean> excessSupplier) {
        return new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excessSupplier);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesLimit) {
        Map<LocalDate, Integer> caloriesPerDay = new HashMap<>();
        for (UserMeal meal : meals) {
            caloriesPerDay.merge(meal.getLocalDate(), meal.getCalories(), Integer::sum);
        }

        List<UserMealWithExcess> result = new ArrayList<>();
        for (UserMeal meal : meals) {
            if (TimeUtil.isBetweenHalfOpen(meal.getLocalTime(), startTime, endTime)) {
                int calories = caloriesPerDay.get(meal.getLocalDate());
                result.add(createUserMealWithExcess(meal, calories > caloriesLimit));
            }
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesLimit) {
        Map<LocalDate, Integer> caloriesPerDay = meals.stream()
                .collect(Collectors.toMap(
                        UserMeal::getLocalDate,     // keyMapper
                        UserMeal::getCalories,      // valueMapper
                        Integer::sum));             // mergeFunction

        return meals.stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getLocalTime(), startTime, endTime))
                .map(meal -> createUserMealWithExcess(meal, caloriesPerDay.get(meal.getLocalDate()) > caloriesLimit))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByOneCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesLimit) {
        List<UserMealWithExcess> result = new ArrayList<>();
        Map<LocalDate, Integer> caloriesPerDay = new HashMap<>();
        for (UserMeal meal : meals) {
            caloriesPerDay.merge(meal.getLocalDate(), meal.getCalories(), Integer::sum);
            if (TimeUtil.isBetweenHalfOpen(meal.getLocalTime(), startTime, endTime)) {
                result.add(createUserMealWithExcess(meal, () -> caloriesPerDay.get(meal.getLocalDate()) > caloriesLimit));
            }
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByOneStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesLimit) {
        return meals.stream()
                .collect(Collectors.groupingBy(UserMeal::getLocalDate)).values().stream()
                .flatMap(oneDay -> {
                    int calories = oneDay.stream().mapToInt(UserMeal::getCalories).sum();
                    return oneDay.stream()
                            .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getLocalTime(), startTime, endTime))
                            .map(meal -> createUserMealWithExcess(meal, calories > caloriesLimit));
                })
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByOneStreamsWithCollector(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesLimit) {
        return meals.stream()
                .collect(new CollectorToUserMealWithExcess(startTime, endTime, caloriesLimit));
    }

}