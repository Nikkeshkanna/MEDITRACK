-- ============================================================
-- MEDITRACK DATABASE - TABLE: hospitals
-- ============================================================

USE meditrack_db;

DROP TABLE IF EXISTS hospitals;

CREATE TABLE hospitals (
    id                      BIGINT          NOT NULL AUTO_INCREMENT,
    name                    VARCHAR(200)    NOT NULL,
    district                VARCHAR(100)    NOT NULL,
    address                 TEXT            NOT NULL,
    phone                   VARCHAR(20),
    email                   VARCHAR(150),
    registration_number     VARCHAR(100)    UNIQUE,
    type                    ENUM(
                                'GOVERNMENT',
                                'PRIVATE',
                                'TRUST',
                                'CLINIC'
                            )               DEFAULT 'GOVERNMENT',
    total_beds              INT             DEFAULT 0,
    active                  TINYINT(1)      NOT NULL DEFAULT 1,
    created_at              DATETIME        DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    INDEX idx_hospitals_district    (district),
    INDEX idx_hospitals_active      (active),
    INDEX idx_hospitals_type        (type)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='All registered hospitals connected to MediTrack';