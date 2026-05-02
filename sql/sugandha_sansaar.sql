-- ============================================================
-- Sugandha Sansaar Database — Schema
-- ============================================================
-- Run this file FIRST in phpMyAdmin to create the database
-- and tables. Then run seed.sql to add sample data.
--
-- Tables:
--   1. roles    — stores admin and user roles
--   2. users    — stores registered accounts (FK → roles)
--   3. sessions — stores active login sessions (FK → users)
-- ============================================================

-- Create and Use Database
CREATE DATABASE IF NOT EXISTS sugandha_sansaar;
USE sugandha_sansaar;

-- Drop existing tables for a clean install
-- (order matters: sessions → users → roles, because of foreign keys)
DROP TABLE IF EXISTS sessions;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;

-- ============================================================
-- Roles table — stores account types (admin, user)
-- ============================================================
CREATE TABLE roles (
                       id          INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
                       role_name   VARCHAR(50)  NOT NULL UNIQUE,
                       description VARCHAR(255) NULL,
                       created_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
                       updated_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ============================================================
-- Users table — stores registered accounts
-- Password column stores SHA2/BCrypt hash, NOT plaintext
-- phone is UNIQUE to prevent duplicate accounts
-- ============================================================
CREATE TABLE users (
                       id            INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
                       role_id       INT          NOT NULL,
                       full_name     VARCHAR(150) NOT NULL,
                       email         VARCHAR(255) NOT NULL UNIQUE,
                       phone         VARCHAR(20)  NOT NULL UNIQUE,
                       password      VARCHAR(255) NOT NULL,
                       profile_pic   VARCHAR(255) NULL,
                       is_active     TINYINT(1)   NOT NULL DEFAULT 1,
                       created_at    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
                       updated_at    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       FOREIGN KEY (role_id) REFERENCES roles(id)
                           ON UPDATE CASCADE
                           ON DELETE RESTRICT
);

-- ============================================================
-- Sessions table — stores active login sessions per user
-- session_token is a unique hash generated at login time
-- expires_at controls how long the session stays valid
-- Deleted automatically when user is deleted (CASCADE)
-- ============================================================
CREATE TABLE sessions (
                          id            INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
                          session_token VARCHAR(128) NOT NULL UNIQUE,
                          user_id       INT          NOT NULL,
                          expires_at    DATETIME     NOT NULL,
                          created_at    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (user_id) REFERENCES users(id)
                              ON UPDATE CASCADE
                              ON DELETE CASCADE
);