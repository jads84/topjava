package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.ComparationUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.checkAccess;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, Meal> database = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            log.info("save {}", meal);
            database.put(meal.getId(), meal);
            return meal;
        } else {
            Meal old = get(meal.getId(), userId);
            if (old == null) {
                log.info("don't save, access denied ");
                return null;
            }
            // handle case: update, but not present in storage
            log.info("update {}", meal);
            return database.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {}", id);
        return get(id, userId) != null && database.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {}", id);
        Meal meal = database.get(id);
        return (checkAccess(meal, userId)) ? meal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll");
        return database.values().stream()
                .filter(meal -> checkAccess(meal, userId))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getFilteredByDateTime(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, int userId) {
        log.info("getFilteredByDateTime");
        return getAll(userId).stream()
                .filter(meal -> ComparationUtil.isBetween(meal.getDate(), startDate, endDate))
                .filter(meal -> ComparationUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime))
                .collect(Collectors.toList());
    }
}

