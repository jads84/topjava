package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.TimeUtil.isBetweenHalfOpen;

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
                .collect(Collectors.toMap(UserMeal::getLocalDate, UserMeal::getCalories, Integer::sum));

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

    public static List<UserMealWithExcess> filteredByOneStreamsWithCollector(List<UserMeal> meals, LocalTime startTime,
                                                                             LocalTime endTime, int caloriesLimit) {
        class CollectorToUserMealWithExcess implements Collector<UserMeal, List<UserMealWithExcess>, List<UserMealWithExcess>> {
            private final Map<LocalDate, Integer> caloriesPerDay = new HashMap<>();

            @Override
            public Supplier<List<UserMealWithExcess>> supplier() {
                return ArrayList::new;
            }

            @Override
            public BiConsumer<List<UserMealWithExcess>, UserMeal> accumulator() {
                return (meals, meal) -> {
                    caloriesPerDay.merge(meal.getLocalDate(), meal.getCalories(), Integer::sum);
                    if (isBetweenHalfOpen(meal.getLocalTime(), startTime, endTime)) {
                        meals.add(createUserMealWithExcess(meal, () -> caloriesPerDay.get(meal.getLocalDate()) > caloriesLimit));
                    }
                };
            }

            @Override
            public BinaryOperator<List<UserMealWithExcess>> combiner() {
                return (l, r) -> {
                    l.addAll(r);
                    return l;
                };
            }

            @Override
            public Function<List<UserMealWithExcess>, List<UserMealWithExcess>> finisher() {
                return Function.identity();
            }

            @Override
            public Set<Characteristics> characteristics() {
                return EnumSet.of(Characteristics.CONCURRENT);
            }
        }

        return meals.stream()
                .collect(new CollectorToUserMealWithExcess());
    }

    private static UserMealWithExcess createUserMealWithExcess(UserMeal meal, boolean excess) {
        return new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }

    private static UserMealWithExcess createUserMealWithExcess(UserMeal meal, Supplier<Boolean> excessSupplier) {
        return new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excessSupplier);
    }

}