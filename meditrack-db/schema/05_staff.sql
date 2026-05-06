-- ============================================================
-- MEDITRACK DATABASE - TABLE: staff
-- ============================================================

USE meditrack_db;

DROP TABLE IF EXISTS staff;

CREATE TABLE staff (
    id                      BIGINT          NOT NULL AUTO_INCREMENT,
    name                    VARCHAR(150)    NOT NULL,
    email                   VARCHAR(150)    NOT NULL UNIQUE,
    mobile                  VARCHAR(15),
    qualification           VARCHAR(200),
    registration_number     VARCHAR(100),
    department              VARCHAR(150),
    role                    ENUM(
                                'DOCTOR',
                                'NURSE',
                                'LAB_TECHNICIAN',
                                'RECEPTIONIST'
                            )               NOT NULL,
    hospital_id             BIGINT          NOT NULL,
    user_id                 BIGINT          UNIQUE,
    active                  TINYINT(1)      NOT NULL DEFAULT 1,
    created_at              DATETIME        DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    CONSTRAINT fk_staff_hospital
        FOREIGN KEY (hospital_id)
        REFERENCES hospitals(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT fk_staff_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    INDEX idx_staff_hospital    (hospital_id),
    INDEX idx_staff_role        (role),
    INDEX idx_staff_active      (active),
    INDEX idx_staff_email       (email)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='Hospital staff - doctors, nurses, lab technicians, receptionists';