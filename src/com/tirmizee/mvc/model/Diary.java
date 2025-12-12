package com.tirmizee.mvc.model;

import java.sql.Timestamp;
public class Diary {
    private int id;
    private int userId;
    private String diaryDate;
    private String title;
    private String content;
    private byte[] photo;          // ⭐ Фото хранится здесь (BLOB)
    private Timestamp createdAt;

    // Если imageName не используется — можешь удалить
    private String imageName;
    public String getImageName() {
        return imageName;
    }

    // ------ GETTERS & SETTERS ------
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDiaryDate() {
        return diaryDate;
    }
    public void setDiaryDate(String diaryDate) {
        this.diaryDate = diaryDate;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getPhoto() {
        return photo;
    }
    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }


    // ============================
    // ⭐⭐ ГЛАВНОЕ: метод, который говорит,
    // есть ли фото или нет
    // ============================
    private boolean hasPhoto;

    public boolean hasPhoto() { return hasPhoto; }
    public void setHasPhoto(boolean hasPhoto) { this.hasPhoto = hasPhoto; }
   
    
    // ==TRASH

    private int isDeleted;

    public int getIsDeleted() { return isDeleted; }
    public void setIsDeleted(int isDeleted) { this.isDeleted = isDeleted; }

    private String firstImage; // preview image
    public String getFirstImage() { return firstImage; }
    public void setFirstImage(String firstImage) { this.firstImage = firstImage; }
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

