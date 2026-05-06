-- ============================================================
-- MEDITRACK DATABASE - TABLE: scan_reports
-- ============================================================

USE meditrack_db;

DROP TABLE IF EXISTS scan_reports;

CREATE TABLE scan_reports (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    patient_id      BIGINT          NOT NULL,
    visit_id        BIGINT,
    hospital_id     BIGINT,
    scan_type       VARCHAR(150),
    findings        TEXT,
    file_name       VARCHAR(300),
    file_path       VARCHAR(500),
    file_size       BIGINT,
    scan_date       DATE,
    status          ENUM(
                        'PENDING',
                        'IN_PROGRESS',
                        'COMPLETED'
                    )               DEFAULT 'COMPLETED',
    created_at      DATETIME        DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    CONSTRAINT fk_scan_patient
        FOREIGN KEY (patient_id)
        REFERENCES patients(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT fk_scan_visit
        FOREIGN KEY (visit_id)
        REFERENCES visits(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    CONSTRAINT fk_scan_hospital
        FOREIGN KEY (hospital_id)
        REFERENCES hospitals(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    INDEX idx_scan_patient   (patient_id),
    INDEX idx_scan_visit     (visit_id),
    INDEX idx_scan_type      (scan_type),
    INDEX idx_scan_date      (scan_date)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='Radiology and scan reports (X-Ray, MRI, CT Scan, Ultrasound)';