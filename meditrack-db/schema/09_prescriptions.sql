-- ============================================================
-- MEDITRACK DATABASE - TABLE: prescriptions
-- ============================================================

USE meditrack_db;

DROP TABLE IF EXISTS prescriptions;

CREATE TABLE prescriptions (
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    visit_id            BIGINT          NOT NULL,
    doctor_id           BIGINT,
    medicine_name       VARCHAR(200)    NOT NULL,
    dosage              VARCHAR(100),
    frequency           VARCHAR(100),
    duration            VARCHAR(100),
    instructions        TEXT,
    notes               TEXT,
    prescription_date   DATE,
    created_at          DATETIME        DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    CONSTRAINT fk_prescription_visit
        FOREIGN KEY (visit_id)
        REFERENCES visits(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_prescription_doctor
        FOREIGN KEY (doctor_id)
        REFERENCES staff(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    INDEX idx_rx_visit      (visit_id),
    INDEX idx_rx_doctor     (doctor_id),
    INDEX idx_rx_date       (prescription_date)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='Medicines prescribed by doctors per visit';