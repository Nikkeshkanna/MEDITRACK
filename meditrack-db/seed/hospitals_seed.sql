-- ============================================================
-- SEED DATA - hospitals
-- ============================================================

USE meditrack_db;

INSERT INTO hospitals
    (name, district, address, phone, email, registration_number, type, total_beds, active)
VALUES
(
    'City General Hospital',
    'Salem',
    '123 Main Road, Shevapet, Salem, Tamil Nadu - 636001',
    '04272-123456',
    'info@citygeneral.com',
    'TN-MED-2024-001',
    'GOVERNMENT',
    350,
    1
),
(
    'Apollo Speciality Clinic',
    'Salem',
    '45 Omalur Road, Salem, Tamil Nadu - 636004',
    '04272-654321',
    'info@apollosalem.com',
    'TN-MED-2024-002',
    'PRIVATE',
    80,
    1
),
(
    'Namakkal District Hospital',
    'Namakkal',
    '10 Hospital Road, Namakkal, Tamil Nadu - 637001',
    '04286-222333',
    'info@namakkaldh.gov.in',
    'TN-MED-2024-003',
    'GOVERNMENT',
    200,
    1
),
(
    'Sri Ramakrishna Trust Hospital',
    'Coimbatore',
    '395 Sarojini Naidu Road, Coimbatore - 641044',
    '0422-4500000',
    'info@srktrust.com',
    'TN-MED-2024-004',
    'TRUST',
    250,
    1
),
(
    'Erode Medical Centre',
    'Erode',
    '12 Perundurai Road, Erode, Tamil Nadu - 638001',
    '0424-2262626',
    'contact@erodemc.com',
    'TN-MED-2024-005',
    'PRIVATE',
    120,
    1
);