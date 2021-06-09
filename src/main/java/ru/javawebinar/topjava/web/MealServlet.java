package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.MealRepositoryInMemory;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.repository.UserRepositoryConstant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.filteredByStreams;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final MealRepository mealRepository = new MealRepositoryInMemory();
    private static final UserRepository userRepository = new UserRepositoryConstant();
    private static final String SERVLET = "meals";
    private static final String MEALS = "meals.jsp";
    private static final String MEAL = "meal.jsp";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private void create(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug("create");
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        mealRepository.add(dateTime, description, calories);
        response.sendRedirect(SERVLET);
    }

    private void readAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("read");
        List<MealTo> mealsTo = filteredByStreams(mealRepository.getAll(),
                LocalTime.MIN, LocalTime.MAX, userRepository.getAll().get(0).getCaloriesPerDay());
        request.setAttribute("mealsTo", mealsTo);
        request.setAttribute("DATE_TIME_FORMATTER", DATE_TIME_FORMATTER);
        request.getRequestDispatcher(MEALS).forward(request, response);
    }

    private void editById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("edit");
        int id = Integer.parseInt(request.getParameter("id"));
        Meal meal = mealRepository.getById(id);
        request.setAttribute("meal", meal);
        request.getRequestDispatcher(MEAL).forward(request, response);
    }

    // before create
    private void editNew(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("edit");
        request.getRequestDispatcher(MEAL).forward(request, response);
    }

    private void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug("update");
        int id = Integer.parseInt(request.getParameter("id"));
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        mealRepository.update(id, dateTime, description, calories);
        response.sendRedirect(SERVLET);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug("delete");
        int id = Integer.parseInt(request.getParameter("id"));
        mealRepository.delete(id);
        response.sendRedirect(SERVLET);
    }

    private void selectAction(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getParameter("action");
        if (action == null) action = "read all";
        switch (action) {
            case "create":
                create(request, response);
                break;
            case "update":
                update(request, response);
                break;
            case "delete":
                delete(request, response);
                break;
            case "edit":
                editById(request, response);
                break;
            case "editNew":
                editNew(request, response);
                break;
            case "read all":
            default:
                readAll(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("GET method");
        selectAction(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("POST method");
        request.setCharacterEncoding("UTF-8");
        selectAction(request, response);
    }
}
