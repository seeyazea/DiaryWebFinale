package com.tirmizee.mvc.dao;
import java.util.List;
import com.tirmizee.mvc.model.Diary;
public interface DiaryDao {

    List<Diary> findAllByUser(int userId);
    List<String> findDiaryDatesByMonth(int userId, int year, int month);
    List<Diary> findByDate(int userId, String diaryDate);


    void save(Diary diary);

    Diary findById(int id);

    void update(Diary diary);

    void delete(int id);
    int countAllByUser(int userId);
    int countPhotosByUser(int userId);
    Diary findToday(int userId, String today);
    
    List<Diary> searchByKeyword(int userId, String keyword); // search
    List<Diary> findPaged(int userId, int offset, int limit); //pagination
    int countThisMonth(int userId);
    int calculateStreak(int userId);
 // 🗑 Корзина (Recycle Bin)
    void softDelete(int id);                   // мягкое удаление
    void restore(int id);                      // восстановить
    void deleteForever(int id);                // удалить полностью
    List<Diary> findDeleted(int userId);       // получить список удалённых записей

    
}
