package com.tirmizee.mvc.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/settings")
public class SettingsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Проверяем авторизацию
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("loginUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Передаём пользователя при нужде
        req.setAttribute("user", session.getAttribute("loginUser"));

        // Открываем settings.jsp из views
        req.getRequestDispatcher("/WEB-INF/views/settings.jsp")
           .forward(req, resp);
    }
}
