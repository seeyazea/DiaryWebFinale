package com.tirmizee.mvc.controller;

import com.tirmizee.mvc.dao.DiaryDao;
import com.tirmizee.mvc.dao.DiaryDaoImpl;
import com.tirmizee.mvc.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private DiaryDao diaryDao = new DiaryDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            response.sendRedirect("login");
            return;
        }

        int userId = loginUser.getId();

        // ===== STATISTICS =====
        int totalEntries = diaryDao.countAllByUser(userId);
        int photosCount = diaryDao.countPhotosByUser(userId);
        int entriesThisMonth = diaryDao.countThisMonth(userId);
        int streak = diaryDao.calculateStreak(userId);

        request.setAttribute("totalEntries", totalEntries);
        request.setAttribute("photosCount", photosCount);
        request.setAttribute("entriesThisMonth", entriesThisMonth);
        request.setAttribute("streakDays", streak);

        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp")
               .forward(request, response);
        

    }
}
