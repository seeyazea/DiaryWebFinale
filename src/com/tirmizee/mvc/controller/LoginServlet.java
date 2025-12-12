package com.tirmizee.mvc.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.tirmizee.mvc.dao.UserDao;
import com.tirmizee.mvc.dao.UserDaoImpl;
import com.tirmizee.mvc.model.User;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserDao userDao = new UserDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Показываем login.jsp
        request.getRequestDispatcher("/WEB-INF/views/login.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Проверяем пользователя
        User user = userDao.findByEmail(email);

        if (user != null && user.getPasswordHash().equals(password)) {

            // Создаем сессию
            HttpSession session = request.getSession();
            session.setAttribute("loginUser", user);

            // Переходим на dashboard
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        // Ошибка входа
        request.setAttribute("error", "이메일 또는 비밀번호가 올바르지 않습니다.");
        request.getRequestDispatcher("/WEB-INF/views/login.jsp")
               .forward(request, response);
    }
}
