-- ============================================================
-- SEED DATA - staff members
-- All passwords = "Staff@2024"
-- BCrypt hash below
-- ============================================================

USE meditrack_db;

-- ============================================================
-- STEP 1: Insert user accounts for staff
-- Password for all staff: Staff@2024
-- ============================================================

INSERT INTO users
    (name, email, mobile, username, password, role, active)
VALUES
-- Doctors - City General Hospital
('Dr. Ramesh Kumar',    'ramesh.kumar@citygeneral.com',   '9876543201', 'ramesh.kumar',   '$2a$12$hashed_placeholder', 'DOCTOR',         1),
('Dr. Priya Nair',      'priya.nair@citygeneral.com',     '9876543202', 'priya.nair',     '$2a$12$hashed_placeholder', 'DOCTOR',         1),
('Dr. Suresh Babu',     'suresh.babu@citygeneral.com',    '9876543203', 'suresh.babu',    '$2a$12$hashed_placeholder', 'DOCTOR',         1),
('Dr. Meena Devi',      'meena.devi@citygeneral.com',     '9876543204', 'meena.devi',     '$2a$12$hashed_placeholder', 'DOCTOR',         1),

-- Nurses - City General Hospital
('Nurse Kavitha',       'kavitha@citygeneral.com',        '9876543210', 'kavitha.nurse',  '$2a$12$hashed_placeholder', 'NURSE',          1),
('Nurse Anitha',        'anitha@citygeneral.com',         '9876543211', 'anitha.nurse',   '$2a$12$hashed_placeholder', 'NURSE',          1),

-- Lab Technicians
('Senthil Kumar',       'senthil@citygeneral.com',        '9876543220', 'senthil.lab',    '$2a$12$hashed_placeholder', 'LAB_TECHNICIAN', 1),
('Divya Priya',         'divya@citygeneral.com',          '9876543221', 'divya.lab',      '$2a$12$hashed_placeholder', 'LAB_TECHNICIAN', 1),

-- Receptionists
('Rekha Devi',          'rekha@citygeneral.com',          '9876543230', 'rekha.reception','$2a$12$hashed_placeholder', 'RECEPTIONIST',   1),

-- Apollo Clinic Staff
('Dr. Kavitha Mohan',   'kavitha.mohan@apollo.com',       '9876543240', 'kavitha.mohan',  '$2a$12$hashed_placeholder', 'DOCTOR',         1),
('Dr. Balaji Rao',      'balaji.rao@apollo.com',          '9876543241', 'balaji.rao',     '$2a$12$hashed_placeholder', 'DOCTOR',         1),
('Nurse Preethi',       'preethi@apollo.com',             '9876543250', 'preethi.nurse',  '$2a$12$hashed_placeholder', 'NURSE',          1),
('Dinesh Kumar',        'dinesh@apollo.com',              '9876543260', 'dinesh.lab',     '$2a$12$hashed_placeholder', 'LAB_TECHNICIAN', 1),
('Geetha',              'geetha@apollo.com',              '9876543270', 'geetha.reception','$2a$12$hashed_placeholder','RECEPTIONIST',   1);

-- ============================================================
-- STEP 2: Insert staff profiles
-- Make sure user IDs match the inserted users above
-- ============================================================

-- NOTE: Use Admin Panel to add staff properly via the web UI
-- This seed is for reference only.
-- When you add staff via Admin Panel (http://localhost:3000/admin/staff),
-- the system handles both user + staff table insertion automatically.

-- ============================================================
-- IMPORTANT: To use this seed properly, run the backend first.
-- Then login as admin and use "Add Staff" in Admin Panel.
-- The admin will create proper BCrypt-hashed passwords.
-- ============================================================