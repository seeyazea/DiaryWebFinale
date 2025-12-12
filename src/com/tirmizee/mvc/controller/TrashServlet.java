package com.tirmizee.mvc.controller;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.util.List;

import com.tirmizee.mvc.dao.DiaryDao;
import com.tirmizee.mvc.dao.DiaryDaoImpl;
import com.tirmizee.mvc.model.Diary;

@WebServlet("/trash")
public class TrashServlet extends HttpServlet {

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

        List<Diary> trashList = diaryDao.findDeleted(userId);
        request.setAttribute("trashList", trashList);

        request.getRequestDispatcher("/WEB-INF/views/diary/trash.jsp")
               .forward(request, response);
    }
}
