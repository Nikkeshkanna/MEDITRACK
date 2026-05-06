-- ============================================================
-- MEDITRACK DATABASE - TABLE: patients
-- ============================================================

USE meditrack_db;

DROP TABLE IF EXISTS patients;

CREATE TABLE patients (
    id                          BIGINT          NOT NULL AUTO_INCREMENT,
    uhid                        VARCHAR(20)     NOT NULL UNIQUE,
    name                        VARCHAR(150)    NOT NULL,
    mobile                      VARCHAR(15)     NOT NULL UNIQUE,
    email                       VARCHAR(150),
    dob                         DATE,
    gender                      VARCHAR(20),
    blood_group                 VARCHAR(10),
    address                     TEXT,
    allergies                   TEXT,
    chronic_diseases            TEXT,
    current_medications         TEXT,
    emergency_contact_name      VARCHAR(150),
    emergency_contact_mobile    VARCHAR(15),
    emergency_contact_relation  VARCHAR(80),
    photo_url                   VARCHAR(500),
    qr_code_data                TEXT,
    user_id                     BIGINT          UNIQUE,
    created_at                  DATETIME        DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    CONSTRAINT fk_patient_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    INDEX idx_patient_uhid      (uhid),
    INDEX idx_patient_mobile    (mobile),
    INDEX idx_patient_name      (name),
    INDEX idx_patient_blood     (blood_group),
    FULLTEXT INDEX ft_patient_search (name, mobile)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='Patient profiles with UHID, QR code, and medical details';