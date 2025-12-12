package com.tirmizee.mvc.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Завершаем сессию
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Переход на логин
        response.sendRedirect(request.getContextPath() + "/login");
    }
}
