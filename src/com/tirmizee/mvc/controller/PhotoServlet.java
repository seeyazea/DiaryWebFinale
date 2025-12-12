package com.tirmizee.mvc.controller;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.tirmizee.mvc.dao.DiaryDao;
import com.tirmizee.mvc.dao.DiaryDaoImpl;
import com.tirmizee.mvc.model.Diary;

@WebServlet("/photo")
public class PhotoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private DiaryDao diaryDao = new DiaryDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Получаем ID
        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int id = Integer.parseInt(idParam);
        Diary diary = diaryDao.findById(id);

        // Проверка существования
        if (diary == null || diary.getPhoto() == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        byte[] photo = diary.getPhoto();

        // Определяем MIME type по сигнатуре файла
        String mime = detectMimeType(photo);
        response.setContentType(mime);

        response.setContentLength(photo.length);

        // Отправляем изображение
        try (OutputStream os = response.getOutputStream()) {
            os.write(photo);
        }
    }

    /** Определяем тип изображения по первым байтам */
    private String detectMimeType(byte[] bytes) {

        // JPG
        if (bytes.length >= 3 &&
            bytes[0] == (byte)0xFF &&
            bytes[1] == (byte)0xD8 &&
            bytes[2] == (byte)0xFF) {
            return "image/jpeg";
        }

        // PNG
        if (bytes.length >= 8 &&
            bytes[0] == (byte)0x89 &&
            bytes[1] == (byte)0x50 &&
            bytes[2] == (byte)0x4E &&
            bytes[3] == (byte)0x47) {
            return "image/png";
        }

        // WEBP
        if (bytes.length >= 12 &&
            bytes[0] == 'R' && bytes[1] == 'I' && bytes[2] == 'F' && bytes[3] == 'F') {
            return "image/webp";
        }

        // Fallback
        return "application/octet-stream";
    }
}
