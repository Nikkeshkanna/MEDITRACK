-- ============================================================
-- MEDITRACK DATABASE - TABLE: users
-- ============================================================

CREATE DATABASE IF NOT EXISTS meditrack_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE meditrack_db;

DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    name            VARCHAR(150)    NOT NULL,
    email           VARCHAR(150)    UNIQUE,
    mobile          VARCHAR(15)     UNIQUE,
    username        VARCHAR(100)    UNIQUE,
    password        VARCHAR(255)    NOT NULL,
    role            ENUM(
                        'PATIENT',
                        'DOCTOR',
                        'NURSE',
                        'LAB_TECHNICIAN',
                        'RECEPTIONIST',
                        'ADMIN'
                    )               NOT NULL,
    active          TINYINT(1)      NOT NULL DEFAULT 1,
    photo_url       VARCHAR(500),
    created_at      DATETIME        DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME        DEFAULT CURRENT_TIMESTAMP
                                    ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    INDEX idx_users_email   (email),
    INDEX idx_users_mobile  (mobile),
    INDEX idx_users_role    (role),
    INDEX idx_users_active  (active)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='Stores all user accounts for patients, staff, and admin';