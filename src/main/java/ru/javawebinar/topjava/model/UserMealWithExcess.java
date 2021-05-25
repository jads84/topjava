package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public class UserMealWithExcess {

    private final LocalDateTime dateTime;
    private final String description;
    private final int calories;
    private boolean excess;
    private final transient Map<LocalDate, Integer> caloriesPerDay;


    // старый конструктор, не используется, оставил для совместимости
    public UserMealWithExcess(LocalDateTime dateTime, String description, int calories, boolean excess) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
        this.caloriesPerDay = null;
    }


    // Конструктор, который сразу всё знает о текущем приёме пищи. Мапа не нужна
    public UserMealWithExcess(UserMeal meal, boolean excess) {
        this.dateTime = meal.getDateTime();
        this.description = meal.getDescription();
        this.calories = meal.getCalories();
        this.excess = excess;
        this.caloriesPerDay = null;
    }


    // Конструктор для нового функционала, который на этапе создания этого приёма пищи
    // ещё не знает о том, было ли превышение по колориям за этот день или нет.
    // сохраняем в DTOшке ссылку на мапу, в которой считаем привышение
    // При сериализации обновляем поле excess
    public UserMealWithExcess(UserMeal meal, Map<LocalDate, Integer> caloriesPerDay) {
        this.dateTime = meal.getDateTime();
        this.description = meal.getDescription();
        this.calories = meal.getCalories();
        this.caloriesPerDay = caloriesPerDay;
    }


    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public boolean isExcess() {
        if (caloriesPerDay != null) {
            // calories > limit
            return caloriesPerDay.get(dateTime.toLocalDate()) > caloriesPerDay.get(null);
        } else return excess;
    }


    @Override
    public String toString() {
        return "UserMealWithExcess{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + isExcess() +
                '}';
    }
}
