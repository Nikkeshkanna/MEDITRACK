-- ============================================================
-- MEDITRACK DATABASE - TABLE: lab_reports
-- ============================================================

USE meditrack_db;

DROP TABLE IF EXISTS lab_reports;

CREATE TABLE lab_reports (
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    visit_id            BIGINT,
    patient_id          BIGINT          NOT NULL,
    lab_tech_id         BIGINT,
    hospital_id         BIGINT,
    test_type           VARCHAR(150)    NOT NULL,
    remarks             TEXT,
    file_name           VARCHAR(300),
    file_path           VARCHAR(500),
    file_size           BIGINT,
    file_type           VARCHAR(100),
    collection_date     DATE,
    report_date         DATE,
    status              ENUM(
                            'PENDING',
                            'IN_PROGRESS',
                            'COMPLETED'
                        )               DEFAULT 'COMPLETED',
    created_at          DATETIME        DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    CONSTRAINT fk_lab_report_visit
        FOREIGN KEY (visit_id)
        REFERENCES visits(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    CONSTRAINT fk_lab_report_patient
        FOREIGN KEY (patient_id)
        REFERENCES patients(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT fk_lab_report_tech
        FOREIGN KEY (lab_tech_id)
        REFERENCES staff(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    CONSTRAINT fk_lab_report_hospital
        FOREIGN KEY (hospital_id)
        REFERENCES hospitals(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    INDEX idx_lab_patient    (patient_id),
    INDEX idx_lab_visit      (visit_id),
    INDEX idx_lab_status     (status),
    INDEX idx_lab_date       (report_date),
    INDEX idx_lab_type       (test_type)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='Lab test reports uploaded by lab technicians';