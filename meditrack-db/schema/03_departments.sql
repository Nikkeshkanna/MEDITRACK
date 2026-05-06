-- ============================================================
-- MEDITRACK DATABASE - TABLE: departments
-- ============================================================

USE meditrack_db;

DROP TABLE IF EXISTS departments;

CREATE TABLE departments (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    name            VARCHAR(150)    NOT NULL,
    head_doctor     VARCHAR(150),
    floor           VARCHAR(50),
    phone           VARCHAR(30),
    hospital_id     BIGINT          NOT NULL,
    active          TINYINT(1)      NOT NULL DEFAULT 1,

    PRIMARY KEY (id),
    CONSTRAINT fk_dept_hospital
        FOREIGN KEY (hospital_id)
        REFERENCES hospitals(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    INDEX idx_dept_hospital     (hospital_id),
    INDEX idx_dept_name         (name)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='Departments within each hospital';