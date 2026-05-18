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
-- ============================================================

CREATE DATABASE IF NOT EXISTS sugandha_sansaar;
USE sugandha_sansaar;

-- ============================================================
-- Drop all tables in reverse FK order (safe clean re-run)
-- ============================================================
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS cart_items;
DROP TABLE IF EXISTS cart;
DROP TABLE IF EXISTS product_images;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS sessions;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;

-- ============================================================
-- 1. Roles — stores account types (admin, user)
-- ============================================================
CREATE TABLE roles (
                       id          INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
                       role_name   VARCHAR(50)  NOT NULL UNIQUE,
                       description VARCHAR(255) NULL,
                       created_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
                       updated_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ============================================================
-- 2. Users — stores registered accounts
-- password stores BCrypt hash, NOT plaintext
-- phone is UNIQUE to prevent duplicate accounts
-- ============================================================
CREATE TABLE users (
                       id          INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
                       role_id     INT          NOT NULL,
                       full_name   VARCHAR(150) NOT NULL,
                       email       VARCHAR(255) NOT NULL UNIQUE,
                       phone       VARCHAR(20)  NOT NULL UNIQUE,
                       password    VARCHAR(255) NOT NULL,
                       profile_pic VARCHAR(255) NULL,
                       is_active   TINYINT(1)   NOT NULL DEFAULT 1,
                       created_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
                       updated_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       FOREIGN KEY (role_id) REFERENCES roles(id)  --  FK
                           ON UPDATE CASCADE
                           ON DELETE RESTRICT
);

-- ============================================================
-- 3. Sessions — stores active login sessions per user
-- session_token is a unique hash generated at login time
-- Deleted automatically when user is deleted (CASCADE)
-- ============================================================
CREATE TABLE sessions (
                          id            INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
                          session_token VARCHAR(128) NOT NULL UNIQUE,
                          user_id       INT          NOT NULL,
                          expires_at    DATETIME     NOT NULL,
                          created_at    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (user_id) REFERENCES users(id)  --  FK
                              ON UPDATE CASCADE
                              ON DELETE CASCADE
);

-- ============================================================
-- 4. Categories — lookup table for product categories
-- ============================================================
CREATE TABLE categories (
                            id          INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
                            name        VARCHAR(100) NOT NULL UNIQUE,
                            description TEXT         NULL,
                            created_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
                            updated_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ============================================================
-- 5. Products — main product table
-- FIXED: category_id INT FK replaces old plain VARCHAR category
-- FIXED: price and volume changed from DOUBLE to DECIMAL (money safe)
-- FIXED: gender changed to ENUM to restrict invalid values
-- ============================================================
CREATE TABLE products (
                          id          INT                           NOT NULL AUTO_INCREMENT PRIMARY KEY,
                          category_id INT                           NOT NULL,
                          name        VARCHAR(150)                  NOT NULL,
                          brand       VARCHAR(100)                  NOT NULL,
                          description TEXT                          NULL,
                          price       DECIMAL(10,2)                 NOT NULL,
                          stock       INT                           NOT NULL DEFAULT 0,
                          image_url   VARCHAR(500)                  NULL,
                          volume      DECIMAL(6,2)                  NULL,
                          gender      ENUM('male','female') NULL,
                          active      TINYINT(1)                    NOT NULL DEFAULT 1,
                          created_at  TIMESTAMP                     DEFAULT CURRENT_TIMESTAMP,
                          updated_at  TIMESTAMP                     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          FOREIGN KEY (category_id) REFERENCES categories(id)  -- FK
                              ON UPDATE CASCADE
                              ON DELETE RESTRICT
);

-- ============================================================
-- 6. Product Images — multiple images per product
-- sort_order: 1 = main cover image, 2+ = additional views
-- ============================================================
CREATE TABLE product_images (
                                id         INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                product_id INT          NOT NULL,
                                image_url  VARCHAR(500) NOT NULL,
                                sort_order INT          NOT NULL DEFAULT 1,
                                created_at TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
                                FOREIGN KEY (product_id) REFERENCES products(id)  --  FK
                                    ON UPDATE CASCADE
                                    ON DELETE CASCADE
);

-- ============================================================
-- 7. Cart — one cart per user
-- UNIQUE on user_id enforces one-to-one with users
-- ============================================================
CREATE TABLE cart (
                      id         INT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
                      user_id    INT       NOT NULL UNIQUE,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      FOREIGN KEY (user_id) REFERENCES users(id)  --  FK
                          ON UPDATE CASCADE
                          ON DELETE CASCADE
);

-- ============================================================
-- 8. Cart Items — individual products inside a cart
-- unit_price snapshot stored at time of adding to cart
-- ============================================================
CREATE TABLE cart_items (
                            id         INT           NOT NULL AUTO_INCREMENT PRIMARY KEY,
                            cart_id    INT           NOT NULL,
                            product_id INT           NOT NULL,
                            quantity   INT           NOT NULL DEFAULT 1,
                            unit_price DECIMAL(10,2) NOT NULL,
                            created_at TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            FOREIGN KEY (cart_id) REFERENCES cart(id)        --  FK
                                ON UPDATE CASCADE
                                ON DELETE CASCADE,
                            FOREIGN KEY (product_id) REFERENCES products(id) --  FK
                                ON UPDATE CASCADE
                                ON DELETE RESTRICT
);

-- ============================================================
-- 9. Orders — one record per placed order
-- Delivery address embedded as snapshot (no separate table)
-- status flow: pending → processing → shipped → delivered → cancelled
-- ============================================================
CREATE TABLE orders (
                        id                INT                                                         NOT NULL AUTO_INCREMENT PRIMARY KEY,
                        user_id           INT                                                         NOT NULL,
                        order_number      VARCHAR(50)                                                 NOT NULL UNIQUE,
                        delivery_name     VARCHAR(150)                                                NOT NULL,
                        delivery_phone    VARCHAR(20)                                                 NOT NULL,
                        delivery_street   VARCHAR(255)                                                NOT NULL,
                        delivery_city     VARCHAR(100)                                                NOT NULL,
                        delivery_state    VARCHAR(100)                                                NOT NULL,
                        delivery_pin_code VARCHAR(20)                                                 NOT NULL,
                        subtotal          DECIMAL(10,2)                                               NOT NULL,
                        shipping_fee      DECIMAL(10,2)                                               NOT NULL DEFAULT 0.00,
                        total_amount      DECIMAL(10,2)                                               NOT NULL,
                        status            ENUM('pending','processing','shipped','delivered','cancelled') NOT NULL DEFAULT 'pending',
                        ordered_at        TIMESTAMP                                                   DEFAULT CURRENT_TIMESTAMP,
                        updated_at        TIMESTAMP                                                   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES users(id)  --  FK
                            ON UPDATE CASCADE
                            ON DELETE RESTRICT
);

-- ============================================================
-- 10. Order Items — individual products inside an order
-- unit_price and line_total are price snapshots at purchase time
-- ============================================================
CREATE TABLE order_items (
                             id         INT           NOT NULL AUTO_INCREMENT PRIMARY KEY,
                             order_id   INT           NOT NULL,
                             product_id INT           NOT NULL,
                             quantity   INT           NOT NULL,
                             unit_price DECIMAL(10,2) NOT NULL,
                             line_total DECIMAL(10,2) NOT NULL,
                             FOREIGN KEY (order_id) REFERENCES orders(id)     --  FK
                                 ON UPDATE CASCADE
                                 ON DELETE CASCADE,
                             FOREIGN KEY (product_id) REFERENCES products(id) --  FK
                                 ON UPDATE CASCADE
                                 ON DELETE RESTRICT
);

-- ============================================================
-- 11. Payments — one payment record per order
-- UNIQUE on order_id enforces one-to-one with orders
-- method includes Nepal-specific gateways (eSewa, Khalti)
-- status flow: pending → completed → failed → refunded
-- ============================================================
CREATE TABLE payments (
                          id             INT                                              NOT NULL AUTO_INCREMENT PRIMARY KEY,
                          order_id       INT                                             NOT NULL UNIQUE,
                          method         ENUM('cash_on_delivery','esewa','khalti','bank_transfer') NOT NULL,
                          transaction_id VARCHAR(100)                                    NULL,
                          amount         DECIMAL(10,2)                                   NOT NULL,
                          status         ENUM('pending','completed','failed','refunded')  NOT NULL DEFAULT 'pending',
                          paid_at        DATETIME                                         NULL,
                          created_at     TIMESTAMP                                        DEFAULT CURRENT_TIMESTAMP,
                          updated_at     TIMESTAMP                                        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          FOREIGN KEY (order_id) REFERENCES orders(id)  --  FK
                              ON UPDATE CASCADE
                              ON DELETE RESTRICT
);