package com.tirmizee.mvc.controller;

import com.tirmizee.mvc.dao.UserDao;
import com.tirmizee.mvc.dao.UserDaoImpl;
import com.tirmizee.mvc.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/auth")
public class UserAuthServlet extends HttpServlet {

    private UserDao userDao = new UserDaoImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        // LOGOUT
        if ("logout".equals(action)) {

            HttpSession session = req.getSession(false);
            if (session != null) session.invalidate();

            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // По умолчанию — login
        resp.sendRedirect(req.getContextPath() + "/login");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        if ("login".equals(action)) {
            handleLogin(req, resp); return;
        }

        if ("register".equals(action)) {
            handleRegister(req, resp); return;
        }

        resp.sendRedirect(req.getContextPath() + "/login");
    }

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String fullname = req.getParameter("fullname");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        User user = new User();
        user.setFullname(fullname);
        user.setEmail(email);
        user.setPasswordHash(password);

        userDao.register(user);

        resp.sendRedirect(req.getContextPath() + "/login?success=1");
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        User user = userDao.findByEmail(email);

        // Проверка
        if (user != null && password.equals(user.getPasswordHash())) {

            HttpSession session = req.getSession();
            session.setAttribute("loginUser", user);
            session.setAttribute("userId", user.getId());

            // Переход на dashboard
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }

        // Ошибка логина
        resp.sendRedirect(req.getContextPath() + "/login?error=1");
    }
}
