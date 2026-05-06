-- ============================================================
-- MEDITRACK DATABASE - TABLE: visits
-- ============================================================

USE meditrack_db;

DROP TABLE IF EXISTS visits;

CREATE TABLE visits (
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    token_number        VARCHAR(20)     NOT NULL UNIQUE,
    patient_id          BIGINT          NOT NULL,
    hospital_id         BIGINT          NOT NULL,
    doctor_id           BIGINT,
    nurse_id            BIGINT,
    department          VARCHAR(150),
    visit_type          ENUM(
                            'OPD',
                            'EMERGENCY',
                            'FOLLOW_UP',
                            'CONSULTATION'
                        )               DEFAULT 'OPD',
    status              ENUM(
                            'WAITING',
                            'WITH_NURSE',
                            'WITH_DOCTOR',
                            'COMPLETED',
                            'CANCELLED'
                        )               DEFAULT 'WAITING',
    visit_date          DATE            NOT NULL,
    check_in_time       DATETIME,
    check_out_time      DATETIME,
    chief_complaint     TEXT,
    created_at          DATETIME        DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    CONSTRAINT fk_visit_patient
        FOREIGN KEY (patient_id)
        REFERENCES patients(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT fk_visit_hospital
        FOREIGN KEY (hospital_id)
        REFERENCES hospitals(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT fk_visit_doctor
        FOREIGN KEY (doctor_id)
        REFERENCES staff(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    CONSTRAINT fk_visit_nurse
        FOREIGN KEY (nurse_id)
        REFERENCES staff(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    INDEX idx_visit_patient         (patient_id),
    INDEX idx_visit_hospital        (hospital_id),
    INDEX idx_visit_doctor          (doctor_id),
    INDEX idx_visit_date            (visit_date),
    INDEX idx_visit_status          (status),
    INDEX idx_visit_token           (token_number)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='Patient hospital visits - OPD, emergency, follow-up';