package com.tirmizee.mvc.controller;

import com.tirmizee.mvc.dao.UserDao;
import com.tirmizee.mvc.dao.UserDaoImpl;
import com.tirmizee.mvc.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/editProfile")
public class EditProfileServlet extends HttpServlet {

    private UserDao userDao = new UserDaoImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        // LOGIN CHECK
        if (session == null || session.getAttribute("loginUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        req.getRequestDispatcher("/WEB-INF/views/edit_profile.jsp")
           .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession();

        User user = (User) session.getAttribute("loginUser");

        // NEW VALUES
        String fullname = req.getParameter("fullname");
        String email = req.getParameter("email");

        // UPDATE MODEL
        user.setFullname(fullname);
        user.setEmail(email);

        // UPDATE DB
        userDao.update(user);

        // UPDATE SESSION
        session.setAttribute("loginUser", user);

        // REDIRECT to profile (PRG pattern)
        resp.sendRedirect(req.getContextPath() + "/profile");
    }
}
