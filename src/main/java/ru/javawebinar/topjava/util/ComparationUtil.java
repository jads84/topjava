package ru.javawebinar.topjava.util;

public class ComparationUtil {

    public static <T extends Comparable<T>> boolean isBetween(T value, T start, T end) {
        return (start == null || value.compareTo(start) >= 0) &&
                (end == null || value.compareTo(end) <= 0);
    }

    public static <T extends Comparable<T>> boolean isBetweenHalfOpen(T value, T start, T end) {
        return (start == null || value.compareTo(start) >= 0) &&
                (end == null || value.compareTo(end) < 0);
    }

}
