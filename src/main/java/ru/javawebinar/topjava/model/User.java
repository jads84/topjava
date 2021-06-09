package ru.javawebinar.topjava.model;

public class User {
    private final int caloriesPerDay;

    public User(int caloriesPerDay) {
        this.caloriesPerDay = caloriesPerDay;
    }

    public int getCaloriesPerDay() {
        return caloriesPerDay;
    }
}
