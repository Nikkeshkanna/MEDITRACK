-- ============================================================
-- MEDITRACK DATABASE - TABLE: audit_logs
-- ============================================================

USE meditrack_db;

DROP TABLE IF EXISTS audit_logs;

CREATE TABLE audit_logs (
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    action      VARCHAR(100) NOT NULL,
    actor       VARCHAR(150),
    role        ENUM(
                    'PATIENT',
                    'DOCTOR',
                    'NURSE',
                    'LAB_TECHNICIAN',
                    'RECEPTIONIST',
                    'ADMIN'
                ),
    hospital    VARCHAR(200),
    ip_address  VARCHAR(50),
    details     TEXT,
    timestamp   DATETIME    DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    INDEX idx_audit_action      (action),
    INDEX idx_audit_actor       (actor),
    INDEX idx_audit_role        (role),
    INDEX idx_audit_timestamp   (timestamp)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='System-wide audit log for all user actions';