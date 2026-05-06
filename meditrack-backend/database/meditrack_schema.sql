-- ============================================================
-- MediTrack Database Schema
-- MySQL 8.0+
-- User: root | Password: Nikkesh@2006 | DB: meditrack_db
-- ============================================================

CREATE DATABASE IF NOT EXISTS meditrack_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE meditrack_db;

-- ── users ────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS users (
  id         BIGINT       AUTO_INCREMENT PRIMARY KEY,
  name       VARCHAR(150) NOT NULL,
  email      VARCHAR(200) NOT NULL UNIQUE,
  password   VARCHAR(255) NOT NULL,
  role       ENUM('PATIENT','DOCTOR','NURSE') NOT NULL,
  photo      VARCHAR(500),
  is_active  TINYINT(1)  DEFAULT 1,
  created_at DATETIME    DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_users_email (email),
  INDEX idx_users_role  (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── hospitals ─────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS hospitals (
  id             BIGINT       AUTO_INCREMENT PRIMARY KEY,
  name           VARCHAR(200) NOT NULL,
  location       VARCHAR(300),
  address        TEXT,
  city           VARCHAR(100),
  state          VARCHAR(100),
  pincode        VARCHAR(10),
  phone          VARCHAR(20),
  email          VARCHAR(200),
  total_doctors  INT DEFAULT 0,
  total_nurses   INT DEFAULT 0,
  total_beds     INT DEFAULT 0,
  is_active      TINYINT(1) DEFAULT 1,
  created_at     DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_hospitals_city (city)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── hospital_departments ──────────────────────────────────────
CREATE TABLE IF NOT EXISTS hospital_departments (
  hospital_id BIGINT       NOT NULL,
  department  VARCHAR(150) NOT NULL,
  PRIMARY KEY (hospital_id, department),
  FOREIGN KEY (hospital_id) REFERENCES hospitals(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── patients ──────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS patients (
  id                       BIGINT      AUTO_INCREMENT PRIMARY KEY,
  user_id                  BIGINT      NOT NULL UNIQUE,
  uhid                     VARCHAR(20) NOT NULL UNIQUE,
  date_of_birth            DATE,
  gender                   VARCHAR(20),
  blood_group              VARCHAR(10),
  mobile                   VARCHAR(20),
  address                  TEXT,
  emergency_contact_name   VARCHAR(150),
  emergency_contact_number VARCHAR(20),
  allergies                TEXT,
  current_medications      TEXT,
  medical_summary          TEXT,
  created_at               DATETIME    DEFAULT CURRENT_TIMESTAMP,
  updated_at               DATETIME    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  INDEX idx_patients_uhid   (uhid),
  INDEX idx_patients_mobile (mobile)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── doctors ───────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS doctors (
  id                  BIGINT       AUTO_INCREMENT PRIMARY KEY,
  user_id             BIGINT       NOT NULL UNIQUE,
  hospital_id         BIGINT,
  specialization      VARCHAR(150),
  department          VARCHAR(150),
  registration_number VARCHAR(50),
  qualification       VARCHAR(200),
  experience_years    INT,
  is_active           TINYINT(1) DEFAULT 1,
  created_at          DATETIME   DEFAULT CURRENT_TIMESTAMP,
  updated_at          DATETIME   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id)     REFERENCES users    (id) ON DELETE CASCADE,
  FOREIGN KEY (hospital_id) REFERENCES hospitals(id) ON DELETE SET NULL,
  INDEX idx_doctors_hospital (hospital_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── nurses ────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS nurses (
  id                  BIGINT       AUTO_INCREMENT PRIMARY KEY,
  user_id             BIGINT       NOT NULL UNIQUE,
  hospital_id         BIGINT,
  department          VARCHAR(150),
  registration_number VARCHAR(50),
  qualification       VARCHAR(200),
  is_active           TINYINT(1) DEFAULT 1,
  created_at          DATETIME   DEFAULT CURRENT_TIMESTAMP,
  updated_at          DATETIME   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id)     REFERENCES users    (id) ON DELETE CASCADE,
  FOREIGN KEY (hospital_id) REFERENCES hospitals(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── visit_history ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS visit_history (
  id                BIGINT      AUTO_INCREMENT PRIMARY KEY,
  patient_id        BIGINT      NOT NULL,
  hospital_id       BIGINT      NOT NULL,
  doctor_id         BIGINT,
  visit_date        DATE        NOT NULL,
  department        VARCHAR(150),
  diagnosis_summary TEXT,
  treatment_summary TEXT,
  notes             TEXT,
  follow_up_date    DATE,
  visit_status      ENUM('SCHEDULED','IN_PROGRESS','COMPLETED','CANCELLED') DEFAULT 'COMPLETED',
  created_at        DATETIME    DEFAULT CURRENT_TIMESTAMP,
  updated_at        DATETIME    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (patient_id)  REFERENCES patients (id) ON DELETE CASCADE,
  FOREIGN KEY (hospital_id) REFERENCES hospitals(id) ON DELETE RESTRICT,
  FOREIGN KEY (doctor_id)   REFERENCES doctors  (id) ON DELETE SET NULL,
  INDEX idx_visit_patient  (patient_id),
  INDEX idx_visit_hospital (hospital_id),
  INDEX idx_visit_date     (visit_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── prescriptions ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS prescriptions (
  id               BIGINT       AUTO_INCREMENT PRIMARY KEY,
  patient_id       BIGINT       NOT NULL,
  doctor_id        BIGINT,
  hospital_id      BIGINT,
  medicine_name    VARCHAR(200) NOT NULL,
  dosage           VARCHAR(100),
  frequency        VARCHAR(50),
  duration         VARCHAR(100),
  route            VARCHAR(50),
  instructions     TEXT,
  prescribed_date  DATE,
  status           ENUM('ACTIVE','COMPLETED','STOPPED','EXPIRED') DEFAULT 'ACTIVE',
  created_at       DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at       DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (patient_id)  REFERENCES patients (id) ON DELETE CASCADE,
  FOREIGN KEY (doctor_id)   REFERENCES doctors  (id) ON DELETE SET NULL,
  FOREIGN KEY (hospital_id) REFERENCES hospitals(id) ON DELETE SET NULL,
  INDEX idx_rx_patient  (patient_id),
  INDEX idx_rx_status   (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── diagnosis ─────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS diagnosis (
  id                BIGINT AUTO_INCREMENT PRIMARY KEY,
  patient_id        BIGINT NOT NULL,
  doctor_id         BIGINT,
  hospital_id       BIGINT,
  diagnosis_text    TEXT   NOT NULL,
  department        VARCHAR(150),
  treatment_summary TEXT,
  notes             TEXT,
  diagnosis_date    DATE,
  follow_up_date    DATE,
  created_at        DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at        DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (patient_id)  REFERENCES patients (id) ON DELETE CASCADE,
  FOREIGN KEY (doctor_id)   REFERENCES doctors  (id) ON DELETE SET NULL,
  FOREIGN KEY (hospital_id) REFERENCES hospitals(id) ON DELETE SET NULL,
  INDEX idx_diag_patient (patient_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── lab_reports ───────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS lab_reports (
  id          BIGINT       AUTO_INCREMENT PRIMARY KEY,
  patient_id  BIGINT       NOT NULL,
  hospital_id BIGINT,
  doctor_id   BIGINT,
  report_name VARCHAR(200) NOT NULL,
  report_type VARCHAR(100),
  lab_name    VARCHAR(200),
  file_path   VARCHAR(500),
  file_name   VARCHAR(300),
  file_type   VARCHAR(100),
  file_size   BIGINT,
  report_date DATE,
  status      ENUM('PENDING','NORMAL','ABNORMAL','BORDERLINE','REVIEWING') DEFAULT 'PENDING',
  remarks     TEXT,
  created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (patient_id)  REFERENCES patients (id) ON DELETE CASCADE,
  FOREIGN KEY (hospital_id) REFERENCES hospitals(id) ON DELETE SET NULL,
  FOREIGN KEY (doctor_id)   REFERENCES doctors  (id) ON DELETE SET NULL,
  INDEX idx_reports_patient (patient_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── vitals ────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS vitals (
  id                 BIGINT      AUTO_INCREMENT PRIMARY KEY,
  patient_id         BIGINT      NOT NULL,
  nurse_id           BIGINT,
  hospital_id        BIGINT,
  blood_pressure     VARCHAR(30),
  blood_sugar        VARCHAR(30),
  pulse              VARCHAR(20),
  temperature        VARCHAR(20),
  weight             VARCHAR(20),
  height             VARCHAR(20),
  oxygen_saturation  VARCHAR(20),
  respiratory_rate   VARCHAR(20),
  notes              TEXT,
  recorded_at        DATETIME    DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (patient_id)  REFERENCES patients (id) ON DELETE CASCADE,
  FOREIGN KEY (nurse_id)    REFERENCES nurses   (id) ON DELETE SET NULL,
  FOREIGN KEY (hospital_id) REFERENCES hospitals(id) ON DELETE SET NULL,
  INDEX idx_vitals_patient     (patient_id),
  INDEX idx_vitals_recorded_at (recorded_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── emergency_info ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS emergency_info (
  id                         BIGINT  AUTO_INCREMENT PRIMARY KEY,
  patient_id                 BIGINT  NOT NULL UNIQUE,
  blood_group                VARCHAR(10),
  allergies                  TEXT,
  current_medications        TEXT,
  emergency_contact_name     VARCHAR(150),
  emergency_contact_number   VARCHAR(20),
  emergency_contact_relation VARCHAR(50),
  medical_conditions         TEXT,
  organ_donor                TINYINT(1) DEFAULT 0,
  updated_at                 DATETIME   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── opd_records ───────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS opd_records (
  id               BIGINT AUTO_INCREMENT PRIMARY KEY,
  patient_id       BIGINT NOT NULL,
  hospital_id      BIGINT NOT NULL,
  nurse_id         BIGINT,
  doctor_id        BIGINT,
  department       VARCHAR(150),
  priority         ENUM('NORMAL','URGENT','EMERGENCY') DEFAULT 'NORMAL',
  symptoms         TEXT,
  referred_by      VARCHAR(150),
  appointment_date DATE,
  token_number     INT,
  status           ENUM('WAITING','IN_PROGRESS','DONE','CANCELLED') DEFAULT 'WAITING',
  notes            TEXT,
  created_at       DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at       DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (patient_id)  REFERENCES patients (id) ON DELETE CASCADE,
  FOREIGN KEY (hospital_id) REFERENCES hospitals(id) ON DELETE RESTRICT,
  FOREIGN KEY (nurse_id)    REFERENCES nurses   (id) ON DELETE SET NULL,
  FOREIGN KEY (doctor_id)   REFERENCES doctors  (id) ON DELETE SET NULL,
  INDEX idx_opd_hospital_date (hospital_id, appointment_date),
  INDEX idx_opd_patient       (patient_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── notification_logs ─────────────────────────────────────────
CREATE TABLE IF NOT EXISTS notification_logs (
  id                BIGINT  AUTO_INCREMENT PRIMARY KEY,
  patient_id        BIGINT  NOT NULL,
  hospital_id       BIGINT,
  doctor_id         BIGINT,
  hospital_name     VARCHAR(200),
  hospital_location VARCHAR(300),
  doctor_name       VARCHAR(150),
  department        VARCHAR(150),
  visit_date        VARCHAR(30),
  mobile            VARCHAR(20),
  message           TEXT,
  sms_status        ENUM('PENDING','DELIVERED','FAILED') DEFAULT 'PENDING',
  failure_reason    VARCHAR(300),
  sent_at           DATETIME,
  created_at        DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (patient_id)  REFERENCES patients (id) ON DELETE CASCADE,
  FOREIGN KEY (hospital_id) REFERENCES hospitals(id) ON DELETE SET NULL,
  FOREIGN KEY (doctor_id)   REFERENCES doctors  (id) ON DELETE SET NULL,
  INDEX idx_notif_patient (patient_id),
  INDEX idx_notif_status  (sms_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;