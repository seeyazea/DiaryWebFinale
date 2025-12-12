package com.tirmizee.mvc.controller;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

import com.tirmizee.mvc.dao.DiaryDao;
import com.tirmizee.mvc.dao.DiaryDaoImpl;

@WebServlet("/restoreDiary")
public class RestoreDiaryServlet extends HttpServlet {

    private DiaryDao diaryDao = new DiaryDaoImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        diaryDao.restore(id);

        response.sendRedirect("trash");
    }
}
