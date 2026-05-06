-- ============================================================
-- MEDITRACK DATABASE - TABLE: emergency_info
-- ============================================================

USE meditrack_db;

DROP TABLE IF EXISTS emergency_info;

CREATE TABLE emergency_info (
    id                      BIGINT          NOT NULL AUTO_INCREMENT,
    patient_id              BIGINT          NOT NULL UNIQUE,
    contact_name            VARCHAR(150),
    contact_mobile          VARCHAR(15),
    contact_relation        VARCHAR(80),
    blood_group             VARCHAR(10),
    allergies               TEXT,
    chronic_diseases        TEXT,
    current_medications     TEXT,
    doctor_name             VARCHAR(150),
    doctor_phone            VARCHAR(20),

    PRIMARY KEY (id),
    CONSTRAINT fk_emergency_patient
        FOREIGN KEY (patient_id)
        REFERENCES patients(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    INDEX idx_emergency_patient (patient_id)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='Emergency critical info shown when QR is scanned in emergency';