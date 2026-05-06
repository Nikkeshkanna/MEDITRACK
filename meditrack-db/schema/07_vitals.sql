-- ============================================================
-- MEDITRACK DATABASE - TABLE: vitals
-- ============================================================

USE meditrack_db;

DROP TABLE IF EXISTS vitals;

CREATE TABLE vitals (
    id                          BIGINT          NOT NULL AUTO_INCREMENT,
    visit_id                    BIGINT          NOT NULL UNIQUE,
    nurse_id                    BIGINT,
    blood_pressure_systolic     INT,
    blood_pressure_diastolic    INT,
    pulse_rate                  INT,
    temperature                 DECIMAL(5,2),
    weight                      DECIMAL(6,2),
    height                      DECIMAL(6,2),
    bmi                         DECIMAL(5,2),
    oxygen_saturation           INT,
    blood_sugar                 DECIMAL(7,2),
    symptoms                    TEXT,
    nursing_notes               TEXT,
    recorded_at                 DATETIME        DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    CONSTRAINT fk_vitals_visit
        FOREIGN KEY (visit_id)
        REFERENCES visits(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_vitals_nurse
        FOREIGN KEY (nurse_id)
        REFERENCES staff(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    INDEX idx_vitals_visit      (visit_id),
    INDEX idx_vitals_nurse      (nurse_id),
    INDEX idx_vitals_recorded   (recorded_at)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='Patient vital signs recorded by nurses';