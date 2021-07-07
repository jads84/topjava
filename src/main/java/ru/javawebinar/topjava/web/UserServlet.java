package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.RequestUtil.getUserId;

public class UserServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);

    private UserRepository userRepository;

    @Override
    public void init() {
        userRepository = new InMemoryUserRepository();

        userRepository.save(new User(null, "Admin", "admin@mail.ru", "root", Role.ADMIN));
        userRepository.save(new User(null, "User", "user@mail.ru", "root", Role.USER));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("forward to users");
        request.getRequestDispatcher("/users.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        switch ((action == null) ? "default" : action) {
            case "sign_in":
                log.info("sign_in");
                SecurityUtil.setUserId(getUserId(request));
                response.sendRedirect("meals");
                break;
        }
    }
}
