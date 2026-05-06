-- ============================================================
-- SEED DATA - departments
-- ============================================================

USE meditrack_db;

-- Departments for City General Hospital (hospital_id = 1)
INSERT INTO departments (name, head_doctor, floor, phone, hospital_id, active) VALUES
('General Medicine',    'Dr. Ramesh Kumar',     '1st Floor, Block A', 'Ext. 101', 1, 1),
('Cardiology',          'Dr. Priya Nair',        '2nd Floor, Block B', 'Ext. 201', 1, 1),
('Orthopedics',         'Dr. Suresh Babu',       '3rd Floor, Block C', 'Ext. 301', 1, 1),
('Gynecology',          'Dr. Meena Devi',        '2nd Floor, Block A', 'Ext. 211', 1, 1),
('Pediatrics',          'Dr. Karthik Rajan',     '1st Floor, Block B', 'Ext. 111', 1, 1),
('Neurology',           'Dr. Arun Prabhu',       '4th Floor, Block A', 'Ext. 401', 1, 1),
('Dermatology',         'Dr. Lakshmi Priya',     '1st Floor, Block C', 'Ext. 121', 1, 1),
('ENT',                 'Dr. Vijay Kumar',       '2nd Floor, Block C', 'Ext. 221', 1, 1),
('Ophthalmology',       'Dr. Sangeetha',         '3rd Floor, Block A', 'Ext. 311', 1, 1),
('Emergency',           'Dr. Rajkumar',          'Ground Floor',       'Ext. 100', 1, 1),
('Laboratory',          'Mr. Senthil Kumar',     'Ground Floor, Lab',  'Ext. 150', 1, 1),
('Radiology',           'Dr. Anitha',            'Ground Floor, Scan', 'Ext. 160', 1, 1);

-- Departments for Apollo Clinic (hospital_id = 2)
INSERT INTO departments (name, head_doctor, floor, phone, hospital_id, active) VALUES
('General Medicine',    'Dr. Kavitha Mohan',    '1st Floor', 'Ext. 101', 2, 1),
('Cardiology',          'Dr. Balaji Rao',        '2nd Floor', 'Ext. 201', 2, 1),
('Orthopedics',         'Dr. Murugan',           '2nd Floor', 'Ext. 202', 2, 1),
('Emergency',           'Dr. Harini',            'Ground Floor', 'Ext. 100', 2, 1),
('Laboratory',          'Mr. Dinesh Kumar',      'Ground Floor', 'Ext. 150', 2, 1);