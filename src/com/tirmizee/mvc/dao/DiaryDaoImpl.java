package com.tirmizee.mvc.dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.tirmizee.db.config.DBUtil;
import com.tirmizee.mvc.model.Diary;
public class DiaryDaoImpl extends DBUtil implements DiaryDao {

    // ==============================
    // FIND ALL (NO BLOB)
    // ==============================
    @Override
    public List<Diary> findAllByUser(int userId) {
        List<Diary> list = new ArrayList<>();

        String sql =
            "SELECT id, user_id, diary_date, title, content, created_at, " +
            "       (photo IS NOT NULL) AS has_photo " +
            "FROM diary WHERE user_id = ? AND is_deleted = 0 " +
            "ORDER BY diary_date DESC, id DESC";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(extractDiaryWithoutBlob(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    // ==============================
    // SAVE
    // ==============================
    @Override
    public void save(Diary diary) {

        String sql = "INSERT INTO diary (user_id, diary_date, title, content, photo) VALUES (?,?,?,?,?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, diary.getUserId());
            ps.setString(2, diary.getDiaryDate());
            ps.setString(3, diary.getTitle());
            ps.setString(4, diary.getContent());

            if (diary.getPhoto() != null)
                ps.setBytes(5, diary.getPhoto());
            else
                ps.setNull(5, Types.BLOB);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ==============================
    // FIND BY ID (WITH BLOB)
    // ==============================
    @Override
    public Diary findById(int id) {

        String sql =
            "SELECT id, user_id, diary_date, title, content, created_at, " +
            "       photo, (photo IS NOT NULL) AS has_photo " +
            "FROM diary WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Diary diary = extractDiaryWithBlob(rs);
                diary.setHasPhoto(rs.getBoolean("has_photo"));
                return diary;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ==============================
    // UPDATE
    // ==============================
    @Override
    public void update(Diary diary) {

        String sql =
            "UPDATE diary SET diary_date = ?, title = ?, content = ?, photo = ? WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, diary.getDiaryDate());
            ps.setString(2, diary.getTitle());
            ps.setString(3, diary.getContent());

            if (diary.getPhoto() != null)
                ps.setBytes(4, diary.getPhoto());
            else
                ps.setNull(4, Types.BLOB);

            ps.setInt(5, diary.getId());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ==============================
    // DELETE
    // ==============================
    @Override
    public void delete(int id) {
        String sql = "DELETE FROM diary WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ==============================
    // COUNT ENTRIES
    // ==============================
    @Override
    public int countAllByUser(int userId) {

        String sql = "SELECT COUNT(*) FROM diary WHERE user_id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public int countPhotosByUser(int userId) {

        String sql =
            "SELECT COUNT(*) FROM diary " +
            "WHERE user_id = ? AND photo IS NOT NULL AND is_deleted = 0";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }


    // ==============================
    // FIND TODAY
    // ==============================
    @Override
    public Diary findToday(int userId, String today) {

        String sql =
            "SELECT id, user_id, diary_date, title, content, created_at, " +
            "       (photo IS NOT NULL) AS has_photo " +
            "FROM diary WHERE user_id = ? AND diary_date = ? LIMIT 1";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, today);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractDiaryWithoutBlob(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

 // ==============================
 // PAGINATION (исправлено — добавлен фильтр is_deleted = 0)
 // ==============================
 @Override
 public List<Diary> findPaged(int userId, int offset, int limit) {
     List<Diary> list = new ArrayList<>();

     String sql =
         "SELECT id, user_id, diary_date, title, content, created_at, " +
         "       (photo IS NOT NULL) AS has_photo " +
         "FROM diary " +
         "WHERE user_id = ? AND is_deleted = 0 " +     // ⭐ ОБЯЗАТЕЛЬНО! фильтруем удалённые
         "ORDER BY diary_date DESC, id DESC " +
         "LIMIT ?, ?";

     try (Connection con = getConnection();
          PreparedStatement ps = con.prepareStatement(sql)) {

         ps.setInt(1, userId);
         ps.setInt(2, offset);
         ps.setInt(3, limit);

         ResultSet rs = ps.executeQuery();

         while (rs.next()) {
             list.add(extractDiaryWithoutBlob(rs));
         }

     } catch (Exception e) {
         e.printStackTrace();
     }

     return list;
 }


    // ==============================
    // SEARCH
    // ==============================
    @Override
    public List<Diary> searchByKeyword(int userId, String keyword) {
        List<Diary> list = new ArrayList<>();

        String sql =
            "SELECT id, user_id, diary_date, title, content, created_at, " +
            "       (photo IS NOT NULL) AS has_photo " +
            "FROM diary WHERE user_id = ? " +
            "AND (title LIKE ? OR content LIKE ?) " +
            "ORDER BY diary_date DESC";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String like = "%" + keyword + "%";

            ps.setInt(1, userId);
            ps.setString(2, like);
            ps.setString(3, like);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(extractDiaryWithoutBlob(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    // ==============================
    // FIND BY DATE (CALENDAR)
    // ==============================
    @Override
    public List<Diary> findByDate(int userId, String diaryDate) {

        List<Diary> list = new ArrayList<>();

        String sql =
            "SELECT id, user_id, diary_date, title, content, created_at, " +
            "       (photo IS NOT NULL) AS has_photo " +
            "FROM diary " +
            "WHERE user_id = ? AND diary_date = ? AND is_deleted = 0 " +  // ⭐ фильтр! 
            "ORDER BY id DESC";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, diaryDate);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(extractDiaryWithoutBlob(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    // ==============================
    // FIND DATES FOR CALENDAR
    // ==============================
    @Override
    public List<String> findDiaryDatesByMonth(int userId, int year, int month) {

        List<String> dates = new ArrayList<>();

        String sql =
            "SELECT diary_date AS d " +
            "FROM diary " +
            "WHERE user_id = ? " +
            "AND YEAR(diary_date) = ? " +
            "AND MONTH(diary_date) = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, year);
            ps.setInt(3, month);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                dates.add(rs.getString("d"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dates;
    }


    // ==============================
    // COUNT THIS MONTH
    // ==============================
    @Override
    public int countThisMonth(int userId) {

        String sql =
            "SELECT COUNT(*) FROM diary " +
            "WHERE user_id = ? " +
            "AND MONTH(diary_date) = MONTH(NOW()) " +
            "AND YEAR(diary_date) = YEAR(NOW())";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next())
                return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }


    // ==============================
    // STREAK LOGIC
    // ==============================
    @Override
    public int calculateStreak(int userId) {

        String sql =
            "SELECT DISTINCT diary_date FROM diary " +
            "WHERE user_id = ? AND is_deleted = 0 " +
            "ORDER BY diary_date DESC";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            int streak = 0;

            java.time.LocalDate today = java.time.LocalDate.now();
            java.time.LocalDate expected = today;

            while (rs.next()) {

                java.time.LocalDate date = java.time.LocalDate.parse(
                    rs.getString("diary_date")
                );

                if (date.equals(expected)) {     // сегодня → отлично
                    streak++;
                    expected = expected.minusDays(1);
                } 
                else if (date.isBefore(expected)) { // пропуск → конец
                    break;
                }
            }

            return streak;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    
    //softDekete 
    @Override
    public void softDelete(int id) {

        String sql = "UPDATE diary SET is_deleted = 1 WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    //restore
    @Override
    public void restore(int id) {

        String sql = "UPDATE diary SET is_deleted = 0 WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 // delete forever
    @Override
    public void deleteForever(int id) {

        String sql = "DELETE FROM diary WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //findDeleted (получить список удалённых записей)
    @Override
    public List<Diary> findDeleted(int userId) {

        List<Diary> list = new ArrayList<>();

        String sql =
            "SELECT id, user_id, diary_date, title, content, created_at, " +
            "       (photo IS NOT NULL) AS has_photo " +
            "FROM diary WHERE user_id = ? AND is_deleted = 1 " +
            "ORDER BY diary_date DESC, id DESC";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(extractDiaryWithoutBlob(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    // ==============================
    // HELPERS
    // ==============================
    private Diary extractDiaryWithoutBlob(ResultSet rs) throws SQLException {

        Diary diary = new Diary();

        diary.setId(rs.getInt("id"));
        diary.setUserId(rs.getInt("user_id"));
        diary.setDiaryDate(rs.getString("diary_date"));
        diary.setTitle(rs.getString("title"));
        diary.setContent(rs.getString("content"));

        Timestamp ts = rs.getTimestamp("created_at");
        diary.setCreatedAt(ts);

        diary.setHasPhoto(rs.getBoolean("has_photo"));
     // NEW: Auto-detect first image inside HTML content
        String img = extractFirstImage(diary.getContent());
        diary.setFirstImage(img);


        return diary;
    }

    // ===============
    // findFirstImage
    // ===============
    private Diary extractDiaryWithBlob(ResultSet rs) throws SQLException {
        Diary diary = extractDiaryWithoutBlob(rs);
        diary.setPhoto(rs.getBytes("photo"));
        return diary;
    }
    
    private String extractFirstImage(String html) {
        if (html == null) return null;

        int imgIndex = html.indexOf("<img");
        if (imgIndex == -1) return null;

        int srcIndex = html.indexOf("src=\"", imgIndex);
        if (srcIndex == -1) return null;

        int start = srcIndex + 5;
        int end = html.indexOf("\"", start);

        if (end == -1) return null;

        return html.substring(start, end);
    }

}
