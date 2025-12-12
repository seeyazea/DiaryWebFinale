//package com.tirmizee.mvc.controller;
//import com.tirmizee.mvc.dao.DiaryDaoImpl;
//import com.tirmizee.mvc.model.Diary;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.MultipartConfig;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.*;
//import java.io.IOException;
//import java.io.InputStream;
//@WebServlet("/diaryUpdate")
//@MultipartConfig(maxFileSize = 20 * 1024 * 1024)  // 20MB
//public class DiaryUpdateServlet extends HttpServlet {

  //  private static final long serialVersionUID = 1L;

 //   private DiaryDaoImpl diaryDao = new DiaryDaoImpl();

 //   @Override
 //   protected void doPost(HttpServletRequest request, HttpServletResponse response)
 //           throws ServletException, IOException {

 //       request.setCharacterEncoding("UTF-8");

        // 1. Получаем ID записи
   //     int id = Integer.parseInt(request.getParameter("id"));

        // 2. Загружаем текущую запись из БД (чтобы не потерять старое фото)
  //      Diary existingDiary = diaryDao.findById(id);
  //      if (existingDiary == null) {
  //          response.sendRedirect("diaryList");
  //          return;
  //      }

        // 3. Получаем новые значения из формы
  //      String diaryDate = request.getParameter("diaryDate");
  //      String title = request.getParameter("title");
  //      String content = request.getParameter("content");

        // 4. Проверяем, загружено ли новое фото
  //      Part photoPart = request.getPart("photo");
  //      byte[] newPhotoBytes = null;

  //      if (photoPart != null && photoPart.getSize() > 0) {
  //          try (InputStream is = photoPart.getInputStream()) {
  //              newPhotoBytes = is.readAllBytes();  // новое фото
  //          }
  //      }

        // 5. Создаём объект Diary для обновления
  //      Diary diary = new Diary();
  //      diary.setId(id);
  //      diary.setUserId(existingDiary.getUserId()); 
  //      diary.setDiaryDate(diaryDate);
  //      diary.setTitle(title);
  //      diary.setContent(content);

//        // 6. Если новое фото загружено → сохраняем его, иначе используем старое
//        if (newPhotoBytes != null) {
//            diary.setPhoto(newPhotoBytes);
//        } else {
         //   diary.setPhoto(existingDiary.getPhoto()); // оставляем старое фото
        //}

        // 7. Обновляем запись в БД
       // diaryDao.update(diary);

        // 8. Возврат к просмотру записи
     //   response.sendRedirect("diaryView?id=" + id);
   // }
// }
