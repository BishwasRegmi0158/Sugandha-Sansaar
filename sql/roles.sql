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
