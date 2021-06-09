package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.MealRepositoryConstant;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.repository.UserRepositoryConstant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.filteredByStreams;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final MealRepository mealRepository = new MealRepositoryConstant();
    private static final UserRepository userRepository = new UserRepositoryConstant();
    private static final String MEALS = "meals.jsp";
    private static final String MEAL = "meal.jsp";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        log.debug("forward to meals.jsp");
        log.debug("doGet");
        String forward = "";
        String action = request.getParameter("action");
        if ("create".equalsIgnoreCase(action)) {
            log.debug("create meal");
        } else if ("read".equalsIgnoreCase(action)) {
            log.debug("read meal");
            long mealId = Long.parseLong(request.getParameter("mealId"));
            forward = MEAL;
            request.setAttribute("mealTo", mealRepository.getMealById(mealId));
        } else if ("update".equalsIgnoreCase(action)) {
            log.debug("update meal");

        } else if ("delete".equalsIgnoreCase(action)) {
            log.debug("delete meal");
            long mealId = Long.parseLong(request.getParameter("mealId"));
            mealRepository.delete(mealId);
            forward = MEALS;
            List<MealTo> mealTo = filteredByStreams(mealRepository.getAll(),
                    LocalTime.MIN, LocalTime.MAX, userRepository.getAll().get(0).getCaloriesPerDay());
            request.setAttribute("mealsTo", mealTo);
        } else {
            log.debug("list meals");
            forward = MEALS;
            List<MealTo> mealTo = filteredByStreams(mealRepository.getAll(),
                    LocalTime.MIN, LocalTime.MAX, userRepository.getAll().get(0).getCaloriesPerDay());
            request.setAttribute("mealsTo", mealTo);
            request.setAttribute("DATE_TIME_FORMATTER", DATE_TIME_FORMATTER);
        }

        request.getRequestDispatcher(forward).forward(request, response);
    }
}
