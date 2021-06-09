package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.List;

public class UserRepositoryConstant implements UserRepository {

    public static final int CALORIES_PER_DAY = 2000;
    private static final List<User> users = Arrays.asList(
            new User(CALORIES_PER_DAY)
    );

    @Override
    public List<User> getAll() {
        return users;
    }
}
