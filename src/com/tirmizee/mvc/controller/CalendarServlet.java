package com.tirmizee.mvc.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.tirmizee.mvc.dao.DiaryDao;
import com.tirmizee.mvc.dao.DiaryDaoImpl;

@WebServlet("/calendar")
public class CalendarServlet extends HttpServlet {

    private DiaryDao diaryDao = new DiaryDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("login");
            return;
        }

        String yearParam = request.getParameter("year");
        String monthParam = request.getParameter("month");

        LocalDate today = LocalDate.now();
        int year = (yearParam == null) ? today.getYear() : Integer.parseInt(yearParam);
        int month = (monthParam == null) ? today.getMonthValue() : Integer.parseInt(monthParam);

        List<String> diaryDates = diaryDao.findDiaryDatesByMonth(userId, year, month);

        request.setAttribute("year", year);
        request.setAttribute("month", month);
        request.setAttribute("diaryDates", diaryDates);

        request.getRequestDispatcher("/WEB-INF/views/calendar.jsp").forward(request, response);
    }
}
