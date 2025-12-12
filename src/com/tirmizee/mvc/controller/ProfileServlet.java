package com.tirmizee.mvc.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.tirmizee.mvc.model.User;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Проверка сессии
        HttpSession session = req.getSession(false);

        if (session == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Передаём пользователя в JSP
        req.setAttribute("user", loginUser);

        // Открываем страницу профиля
        req.getRequestDispatcher("/WEB-INF/views/profile.jsp")
           .forward(req, resp);
    }
}
