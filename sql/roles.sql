-- ============================================================
-- Sugandha Sansaar — Seed Data
-- Run this AFTER schema.sql
-- Password : Admin@1234
-- ============================================================

USE sugandha_sansaar;

-- Insert roles (INSERT IGNORE skips if already exists)
INSERT IGNORE INTO roles (role_name, description) VALUES
                                                      ('admin', 'Administrator with full system access'),
                                                      ('user',  'Regular user with limited access');

-- Insert admin user (INSERT IGNORE skips if already exists)
INSERT IGNORE INTO users (role_id, full_name, email, phone, password, profile_pic, is_active)
VALUES (
           1,
           'Bishwash Regmi',
           'bishwash@sugandha.com',
           '9860573543',
           '$2a$12$2vxBqpTNBHDyHPLVnsFNJOX7rBqtgKnFfucORUHDoKfGBmHq2OJ1O',
           NULL,
           1
       );