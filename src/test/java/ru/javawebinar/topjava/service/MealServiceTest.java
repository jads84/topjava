package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static ru.javawebinar.topjava.MealTestData.MEAL_ID;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;


@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService mealService;

    @Test
    public void create() {
        Meal expected = new Meal(LocalDateTime.of(2000, Month.JANUARY, 1, 0, 0), "Новогодний стол", 1000);
        Meal actual1 = mealService.create(expected, USER_ID);
        expected.setId(actual1.getId());
        Meal actual2 = mealService.get(actual1.getId(), USER_ID);

        assertThat(actual1).isEqualTo(expected);
        assertThat(actual2).isEqualTo(expected);
    }

    @Test
    public void get() {
        Meal meal = mealService.get(MEAL_ID, USER_ID);
        assertThat(meal).isEqualTo(MealTestData.meal);
    }

    @Test
    public void getAccessDenied() {
        assertThatThrownBy(() -> mealService.get(MEAL_ID, ADMIN_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Not found entity with id=%s", MEAL_ID);
    }


    @Test
    public void update() {
        Meal expected = mealService.get(MEAL_ID, USER_ID);
        expected.setDescription("Изменённая еда");
        expected.setCalories(1000);
        expected.setDateTime(LocalDateTime.of(2021, Month.JANUARY, 31, 0, 0));
        mealService.update(expected, USER_ID);
        Meal actual = mealService.get(MEAL_ID, USER_ID);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void delete() {
        mealService.delete(MEAL_ID, USER_ID);
        assertThatThrownBy(() -> mealService.get(MEAL_ID, USER_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Not found entity with id=%s", MEAL_ID);
    }


    @Test
    public void duplicateDateTimeForOneUser() {
        Meal meal1 = new Meal(LocalDateTime.of(2022, Month.JANUARY, 31, 0, 0), "test food", 100);
        Meal meal2 = new Meal(LocalDateTime.of(2022, Month.JANUARY, 31, 0, 0), "test food", 100);
        mealService.create(meal1, USER_ID);
        assertThatThrownBy(() -> mealService.create(meal2, USER_ID))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    public void deleteNotFoundMeal() {
        mealService.delete(MEAL_ID, USER_ID);
        assertThatThrownBy(() -> mealService.delete(MEAL_ID, USER_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Not found entity with id=%s", MEAL_ID);
    }


    @Test
    public void getBetweenInclusive() {
        LocalDate start = LocalDate.of(2020, Month.JANUARY, 31);
        LocalDate end = LocalDate.of(2020, Month.JANUARY, 31);
        List<Meal> meals = mealService.getBetweenInclusive(start, end, USER_ID);
        assertThat(meals).hasSize(4);
    }

    @Test
    public void getAll() {
        assertThat(mealService.getAll(USER_ID)).hasSize(7);
        assertThat(mealService.getAll(ADMIN_ID)).hasSize(2);
    }

}