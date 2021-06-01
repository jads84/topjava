package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;
import java.util.function.Supplier;

public class UserMealWithExcess {

    private final LocalDateTime dateTime;
    private final String description;
    private final int calories;
    private final boolean excess;
    private final transient Supplier<Boolean> excessSupplier;

    public UserMealWithExcess(LocalDateTime dateTime, String description, int calories, boolean excess) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
        this.excessSupplier = null;
    }

    public UserMealWithExcess(LocalDateTime dateTime, String description, int calories, Supplier<Boolean> excessGetter) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = false;
        this.excessSupplier = excessGetter;
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
        return (excessSupplier == null) ? excess : excessSupplier.get();
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
