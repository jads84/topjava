package ru.javawebinar.topjava.util;

public class ComparationUtil {

    public static <T extends Comparable<T>> boolean isBetweenHalfOpen(T value, T start, T end) {
        return value.compareTo(start) >= 0 && value.compareTo(end) < 0;
    }

}
