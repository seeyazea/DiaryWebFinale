
//@WebServlet("/diary_images/*")
package com.tirmizee.mvc.controller;

import com.tirmizee.mvc.dao.DiaryDao;
import com.tirmizee.mvc.dao.DiaryDaoImpl;
import com.tirmizee.mvc.model.Diary;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/image")
public class ImageServlet extends HttpServlet {

    private DiaryDao diaryDao = new DiaryDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Diary diary = diaryDao.findById(id);

        if (diary == null || diary.getPhoto() == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType("image/jpeg"); // или image/png если надо

        try (OutputStream os = response.getOutputStream()) {
            os.write(diary.getPhoto());
        }
    }
}
