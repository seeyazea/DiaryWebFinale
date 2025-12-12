package com.tirmizee.mvc.controller;

import com.tirmizee.mvc.model.User;
import com.tirmizee.mvc.model.Diary;
import com.tirmizee.mvc.dao.DiaryDao;
import com.tirmizee.mvc.dao.DiaryDaoImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@WebServlet("/diary")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 10 * 1024 * 1024,
        maxRequestSize = 20 * 1024 * 1024
)
public class DiaryServlet extends HttpServlet {

    private DiaryDao diaryDao = new DiaryDaoImpl();
    

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {

            case "write":
                request.getRequestDispatcher("/WEB-INF/views/diary/write.jsp")
                        .forward(request, response);
                break;

            case "view":
                viewDiary(request, response);
                break;

            case "edit":
                editDiary(request, response);
                break;

            case "deleteConfirm":
                deleteConfirm(request, response);
                break;

            case "delete":   // ⭐ ДОБАВЛЕНО soft delete
                deleteDiary(request, response);
                break;

            case "searchPage":
                request.getRequestDispatcher("/WEB-INF/views/diary/search.jsp")
                        .forward(request, response);
                break;

            case "search":
                performSearch(request, response);
                break;

            default:
                listDiary(request, response);
        }
    }

    // 🔥 SEARCH
    private void performSearch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int userId = ((User)request.getSession().getAttribute("loginUser")).getId();
        String keyword = request.getParameter("keyword");

        List<Diary> results = diaryDao.searchByKeyword(userId, keyword);

        request.setAttribute("keyword", keyword);
        request.setAttribute("results", results);
        request.getRequestDispatcher("/WEB-INF/views/diary/search.jsp")
                .forward(request, response);
    }

    // 🔥 LIST
    private void listDiary(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int userId = ((User) request.getSession().getAttribute("loginUser")).getId();

        // ⭐ 1. Если календарь передал дату — показываем ТОЛЬКО записи этого дня
        String filterDate = request.getParameter("date");

        if (filterDate != null && !filterDate.isEmpty()) {

            List<Diary> diaries = diaryDao.findByDate(userId, filterDate);

            request.setAttribute("diaryList", diaries);
            request.setAttribute("filteredDate", filterDate);
            request.setAttribute("page", 1);
            request.setAttribute("totalPages", 1);

            request.getRequestDispatcher("/WEB-INF/views/diary/list.jsp")
                   .forward(request, response);
            return;
        }

        // ⭐ 2. Если даты нет — обычный список с пагинацией
        int page = 1;
        int limit = 10;

        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        int offset = (page - 1) * limit;

        List<Diary> diaries = diaryDao.findPaged(userId, offset, limit);
        int totalCount = diaryDao.countAllByUser(userId);
        int totalPages = (int) Math.ceil((double) totalCount / limit);

        request.setAttribute("diaryList", diaries);
        request.setAttribute("page", page);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("/WEB-INF/views/diary/list.jsp")
               .forward(request, response);
    }


    // 🔥 VIEW
    private void viewDiary(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Diary d = diaryDao.findById(id);

        request.setAttribute("diary", d);
        request.getRequestDispatcher("/WEB-INF/views/diary/view.jsp")
                .forward(request, response);
    }

    // 🔥 EDIT
    private void editDiary(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Diary d = diaryDao.findById(id);

        request.setAttribute("diary", d);
        request.getRequestDispatcher("/WEB-INF/views/diary/edit.jsp")
                .forward(request, response);
    }

    // 🔥 DELETE CONFIRM
    private void deleteConfirm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Diary d = diaryDao.findById(id);

        request.setAttribute("diary", d);
        request.getRequestDispatcher("/WEB-INF/views/diary/delete.jsp")
                .forward(request, response);
    }

    // ⭐ МЯГКОЕ УДАЛЕНИЕ (soft delete)
    private void deleteDiary(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        // 👇 ВАЖНО: теперь мягкое удаление, НЕ delete()
        diaryDao.softDelete(id);

        response.sendRedirect(request.getContextPath() + "/diary?action=list");
    }


    // =============== POST ===============

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("insert".equals(action)) {
            insertDiary(request, response);
        } else if ("update".equals(action)) {
            updateDiary(request, response);
        } 
        else if ("offlineSync".equals(action)) {   // ⭐ ДОБАВЛЕНО
            offlineSync(request, response);
        } 
        else {
            response.sendRedirect(request.getContextPath() + "/diary");
        }
    }

    // 🔥 INSERT
    private void insertDiary(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int userId = ((User) request.getSession().getAttribute("loginUser")).getId();

        Diary diary = new Diary();
        diary.setUserId(userId);
        diary.setDiaryDate(request.getParameter("diaryDate"));
        diary.setTitle(request.getParameter("title"));
        diary.setContent(request.getParameter("content"));

        byte[] photo = readPhotoFromRequest(request);
        diary.setPhoto(photo);

        diaryDao.save(diary);

        response.sendRedirect(request.getContextPath() + "/diary?action=list");
    }

    // 🔥 UPDATE
    private void updateDiary(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Diary d = diaryDao.findById(id);

        d.setDiaryDate(request.getParameter("diaryDate"));
        d.setTitle(request.getParameter("title"));
        d.setContent(request.getParameter("content"));

        byte[] newPhoto = readPhotoFromRequest(request);
        if (newPhoto != null) {
            d.setPhoto(newPhoto);
        }

        diaryDao.update(d);

        response.sendRedirect(request.getContextPath() + "/diary?action=list");
    }

    // 🔥 Read Upload
    private byte[] readPhotoFromRequest(HttpServletRequest request)
            throws IOException, ServletException {

        Part part = request.getPart("image");
        if (part == null || part.getSize() == 0) {
            return null;
        }

        try (InputStream input = part.getInputStream()) {
            return input.readAllBytes();
        }
    }
 // ===============================
//  ⭐ OFFLINE SYNC 처리
//===============================
private void offlineSync(HttpServletRequest request, HttpServletResponse response)
     throws ServletException, IOException {

 int userId = ((User) request.getSession().getAttribute("loginUser")).getId();

 String diaryDate = request.getParameter("diaryDate");
 String title = request.getParameter("title");
 String content = request.getParameter("content");
 String base64Image = request.getParameter("imageBase64");

 byte[] imageBytes = null;

 // 📌 base64 → byte[]
 if (base64Image != null && !base64Image.isEmpty()) {
     try {
         base64Image = base64Image.substring(base64Image.indexOf(",") + 1);
         imageBytes = java.util.Base64.getDecoder().decode(base64Image);
     } catch (Exception e) {
         e.printStackTrace();
     }
 }

 // 📌 Diary 객체 생성
 Diary diary = new Diary();
 diary.setUserId(userId);
 diary.setDiaryDate(diaryDate);
 diary.setTitle(title);
 diary.setContent(content);
 diary.setPhoto(imageBytes);

 // 📌 DB 저장
 diaryDao.save(diary);

 // 📌 JS Fetch가 정상 응답을 받을 수 있도록 OK 보냄
 response.setStatus(HttpServletResponse.SC_OK);
}

}
