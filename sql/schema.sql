-- ===============================
-- DATABASE
-- ===============================
CREATE DATABASE IF NOT EXISTS diary
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE diary;

-- ===============================
-- USER TABLE
-- ===============================
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ===============================
-- DIARY TABLE
-- ===============================
CREATE TABLE diary (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,

    diary_date DATE NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,

    photo LONGBLOB NULL,
    is_deleted TINYINT(1) DEFAULT 0,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_diary_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

-- ===============================
-- INDEXES (IMPORTANT for performance)
-- ===============================
CREATE INDEX idx_diary_user_date
ON diary(user_id, diary_date);



CREATE INDEX idx_diary_is_deleted
ON diary(is_deleted);
INSERT INTO users (username, password)
VALUES ('testuser', '1234');

INSERT INTO diary (user_id, diary_date, title, content)
VALUES
(1, '2025-12-01', 'First Diary', 'Hello Diary!'),
(1, '2025-12-02', 'Second Diary', 'This is my second entry.');
