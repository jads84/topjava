package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
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
    }

    // return filtered list with excess. Implement by cycles
    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesLimit) {
        // определяем, в какой день сколько суммарно каллорий было съедено
        Map<LocalDate, Integer> caloriesPerDay = new HashMap<>();
        for (UserMeal meal: meals) {
            caloriesPerDay.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum);
        }
//        meals.forEach(meal -> excess.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum));

        // фильтруем список согласно условию
        List<UserMealWithExcess> result = new ArrayList<>();
        for (UserMeal meal: meals) {
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                int calories = caloriesPerDay.get(meal.getDateTime().toLocalDate());
                result.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(),
                        meal.getCalories(), calories > caloriesLimit));
            }
        }
        return result;
    }


    // Implement by streams
    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesLimit) {
        // определяем, в какой день сколько суммарно каллорий было съедено
        Map<LocalDate, Integer> caloriesPerDay = meals.stream()
               .collect(Collectors.toMap(
                       userMeal -> userMeal.getDateTime().toLocalDate(), // keyMapper
                       UserMeal::getCalories,                            // valueMapper
                       Integer::sum));                                   // mergeFunction

        // фильтруем список согласно условию
        return meals.stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        caloriesPerDay.get(meal.getDateTime().toLocalDate()) > caloriesLimit))
                .collect(Collectors.toList());
    }
}
