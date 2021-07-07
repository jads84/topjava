package ru.javawebinar.topjava.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class RequestUtil {

    public static int getMealId(HttpServletRequest request) {
        return getId(request, "id");
    }

    public static int getUserId(HttpServletRequest request) {
        return getId(request, "userId");
    }

    private static int getId(HttpServletRequest request, String param) {
        String paramId = Objects.requireNonNull(request.getParameter(param));
        return Integer.parseInt(paramId);
    }

}
