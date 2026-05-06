-- ============================================================
-- MEDITRACK DATABASE - TABLE: diagnoses
-- ============================================================

USE meditrack_db;

DROP TABLE IF EXISTS diagnoses;

CREATE TABLE diagnoses (
    id                      BIGINT          NOT NULL AUTO_INCREMENT,
    visit_id                BIGINT          NOT NULL UNIQUE,
    doctor_id               BIGINT,
    primary_diagnosis       TEXT            NOT NULL,
    secondary_diagnosis     TEXT,
    symptoms                TEXT,
    clinical_notes          TEXT,
    follow_up_required      TINYINT(1)      DEFAULT 0,
    follow_up_date          DATE,
    follow_up_notes         TEXT,
    created_at              DATETIME        DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    CONSTRAINT fk_diagnosis_visit
        FOREIGN KEY (visit_id)
        REFERENCES visits(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_diagnosis_doctor
        FOREIGN KEY (doctor_id)
        REFERENCES staff(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    INDEX idx_diagnosis_visit   (visit_id),
    INDEX idx_diagnosis_doctor  (doctor_id),
    INDEX idx_diagnosis_followup (follow_up_required)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='Doctor diagnosis and clinical notes per visit';