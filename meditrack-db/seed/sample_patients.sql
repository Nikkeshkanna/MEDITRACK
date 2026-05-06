-- ============================================================
-- SEED DATA - sample patients
-- All passwords = Patient@2024
-- ============================================================

USE meditrack_db;

-- ============================================================
-- STEP 1: Create user accounts for sample patients
-- Password: Patient@2024
-- BCrypt hash of "Patient@2024" with strength 12:
-- $2a$12$K8J2qT4xN6vL9mD1oP7sRuWnIeGcFhAyBzXvQrMtCjH5kS3wUbY0p
-- ============================================================

INSERT INTO users
    (name, email, mobile, username, password, role, active)
VALUES
(
    'Nikkesh Kanna',
    'nikkesh@example.com',
    '9876543100',
    '9876543100',
    '$2a$12$K8J2qT4xN6vL9mD1oP7sRuWnIeGcFhAyBzXvQrMtCjH5kS3wUbY0p',
    'PATIENT',
    1
),
(
    'Priya Sharma',
    'priya.sharma@example.com',
    '9876543101',
    '9876543101',
    '$2a$12$K8J2qT4xN6vL9mD1oP7sRuWnIeGcFhAyBzXvQrMtCjH5kS3wUbY0p',
    'PATIENT',
    1
),
(
    'Rajesh Murugan',
    'rajesh.murugan@example.com',
    '9876543102',
    '9876543102',
    '$2a$12$K8J2qT4xN6vL9mD1oP7sRuWnIeGcFhAyBzXvQrMtCjH5kS3wUbY0p',
    'PATIENT',
    1
),
(
    'Lakshmi Devi',
    'lakshmi@example.com',
    '9876543103',
    '9876543103',
    '$2a$12$K8J2qT4xN6vL9mD1oP7sRuWnIeGcFhAyBzXvQrMtCjH5kS3wUbY0p',
    'PATIENT',
    1
),
(
    'Karthik Raja',
    'karthik.raja@example.com',
    '9876543104',
    '9876543104',
    '$2a$12$K8J2qT4xN6vL9mD1oP7sRuWnIeGcFhAyBzXvQrMtCjH5kS3wUbY0p',
    'PATIENT',
    1
);

-- ============================================================
-- STEP 2: Create patient profiles
-- UHIDs are pre-assigned
-- ============================================================

INSERT INTO patients
(
    uhid, name, mobile, email, dob, gender, blood_group, address,
    allergies, chronic_diseases, current_medications,
    emergency_contact_name, emergency_contact_mobile, emergency_contact_relation,
    user_id
)
VALUES
(
    'MT10000001',
    'Nikkesh Kanna',
    '9876543100',
    'nikkesh@example.com',
    '2000-05-15',
    'Male',
    'B+',
    '45 Anna Nagar, Salem, Tamil Nadu - 636001',
    'None',
    'None',
    'None',
    'Kanna Sr.',
    '9876540001',
    'Father',
    (SELECT id FROM users WHERE mobile = '9876543100')
),
(
    'MT10000002',
    'Priya Sharma',
    '9876543101',
    'priya.sharma@example.com',
    '1992-09-20',
    'Female',
    'O+',
    '12 Gandhi Street, Salem, Tamil Nadu - 636003',
    'Penicillin',
    'Hypertension',
    'Amlodipine 5mg once daily',
    'Suresh Sharma',
    '9876540002',
    'Husband',
    (SELECT id FROM users WHERE mobile = '9876543101')
),
(
    'MT10000003',
    'Rajesh Murugan',
    '9876543102',
    'rajesh.murugan@example.com',
    '1975-03-10',
    'Male',
    'A+',
    '78 Nehru Road, Namakkal, Tamil Nadu - 637001',
    'Sulpha drugs',
    'Type 2 Diabetes, Hypertension',
    'Metformin 500mg twice daily, Losartan 50mg once daily',
    'Meena Murugan',
    '9876540003',
    'Wife',
    (SELECT id FROM users WHERE mobile = '9876543102')
),
(
    'MT10000004',
    'Lakshmi Devi',
    '9876543103',
    'lakshmi@example.com',
    '1988-11-25',
    'Female',
    'AB+',
    '33 Kamarajar Street, Erode, Tamil Nadu - 638001',
    'Aspirin',
    'Asthma',
    'Salbutamol inhaler as needed',
    'Ravi Kumar',
    '9876540004',
    'Father',
    (SELECT id FROM users WHERE mobile = '9876543103')
),
(
    'MT10000005',
    'Karthik Raja',
    '9876543104',
    'karthik.raja@example.com',
    '1995-07-08',
    'Male',
    'O-',
    '22 Periyar Nagar, Coimbatore, Tamil Nadu - 641001',
    'None',
    'None',
    'None',
    'Rajan',
    '9876540005',
    'Father',
    (SELECT id FROM users WHERE mobile = '9876543104')
);

-- ============================================================
-- STEP 3: Sample Visits
-- ============================================================

INSERT INTO visits
(
    token_number, patient_id, hospital_id, department,
    visit_type, status, visit_date, check_in_time, chief_complaint
)
VALUES
(
    'T001',
    (SELECT id FROM patients WHERE uhid = 'MT10000001'),
    1,
    'General Medicine',
    'OPD',
    'COMPLETED',
    DATE_SUB(CURDATE(), INTERVAL 30 DAY),
    DATE_SUB(NOW(), INTERVAL 30 DAY),
    'Fever and body pain for 2 days'
),
(
    'T002',
    (SELECT id FROM patients WHERE uhid = 'MT10000002'),
    1,
    'Cardiology',
    'OPD',
    'COMPLETED',
    DATE_SUB(CURDATE(), INTERVAL 15 DAY),
    DATE_SUB(NOW(), INTERVAL 15 DAY),
    'Chest discomfort and palpitations'
),
(
    'T003',
    (SELECT id FROM patients WHERE uhid = 'MT10000003'),
    2,
    'General Medicine',
    'FOLLOW_UP',
    'COMPLETED',
    DATE_SUB(CURDATE(), INTERVAL 7 DAY),
    DATE_SUB(NOW(), INTERVAL 7 DAY),
    'Diabetes follow-up, blood sugar monitoring'
),
(
    'T004',
    (SELECT id FROM patients WHERE uhid = 'MT10000001'),
    1,
    'General Medicine',
    'OPD',
    'WAITING',
    CURDATE(),
    NOW(),
    'Cough and cold'
);

-- ============================================================
-- STEP 4: Sample Vitals
-- ============================================================

INSERT INTO vitals
(
    visit_id, blood_pressure_systolic, blood_pressure_diastolic,
    pulse_rate, temperature, weight, height, bmi,
    oxygen_saturation, blood_sugar, symptoms
)
VALUES
(
    (SELECT id FROM visits WHERE token_number = 'T001'),
    120, 80, 88, 101.2, 68.5, 172.0, 23.2, 98, 95.0,
    'Fever 101.2F, body ache, mild headache'
),
(
    (SELECT id FROM visits WHERE token_number = 'T002'),
    145, 92, 96, 98.6, 72.0, 163.0, 27.1, 97, 105.0,
    'Chest tightness, palpitations during exertion'
),
(
    (SELECT id FROM visits WHERE token_number = 'T003'),
    132, 84, 78, 98.4, 80.0, 168.0, 28.3, 99, 186.0,
    'Blood sugar high, feeling tired'
);

-- ============================================================
-- STEP 5: Sample Diagnoses
-- ============================================================

INSERT INTO diagnoses
(
    visit_id, primary_diagnosis, secondary_diagnosis,
    symptoms, clinical_notes,
    follow_up_required, follow_up_date
)
VALUES
(
    (SELECT id FROM visits WHERE token_number = 'T001'),
    'Viral Fever',
    NULL,
    'Fever 101.2F, body ache, headache',
    'Patient presented with 2-day history of fever. No signs of infection. Advised rest and hydration.',
    0,
    NULL
),
(
    (SELECT id FROM visits WHERE token_number = 'T002'),
    'Hypertension - Stage 1',
    'Anxiety Disorder',
    'Chest tightness, palpitations',
    'BP 145/92. ECG normal. Advised lifestyle modification. Started on Amlodipine.',
    1,
    DATE_ADD(CURDATE(), INTERVAL 30 DAY)
),
(
    (SELECT id FROM visits WHERE token_number = 'T003'),
    'Type 2 Diabetes Mellitus - Uncontrolled',
    'Hypertension',
    'High blood sugar, fatigue',
    'HbA1c 8.2%. Increased Metformin dose. Referred to dietitian.',
    1,
    DATE_ADD(CURDATE(), INTERVAL 14 DAY)
);

-- ============================================================
-- STEP 6: Sample Prescriptions
-- ============================================================

INSERT INTO prescriptions
(
    visit_id, medicine_name, dosage, frequency,
    duration, instructions, prescription_date
)
VALUES
-- Visit T001 - Viral Fever
(
    (SELECT id FROM visits WHERE token_number = 'T001'),
    'Paracetamol 500mg', '1 tablet', 'Three times daily',
    '5 days', 'Take after food', CURDATE() - INTERVAL 30 DAY
),
(
    (SELECT id FROM visits WHERE token_number = 'T001'),
    'Cetirizine 10mg', '1 tablet', 'Once daily at bedtime',
    '3 days', 'May cause drowsiness', CURDATE() - INTERVAL 30 DAY
),
(
    (SELECT id FROM visits WHERE token_number = 'T001'),
    'Vitamin C 500mg', '1 tablet', 'Twice daily',
    '7 days', 'Take after food', CURDATE() - INTERVAL 30 DAY
),

-- Visit T002 - Hypertension
(
    (SELECT id FROM visits WHERE token_number = 'T002'),
    'Amlodipine 5mg', '1 tablet', 'Once daily',
    '30 days', 'Take at same time daily', CURDATE() - INTERVAL 15 DAY
),
(
    (SELECT id FROM visits WHERE token_number = 'T002'),
    'Aspirin 75mg', '1 tablet', 'Once daily after breakfast',
    '30 days', 'Do not take on empty stomach', CURDATE() - INTERVAL 15 DAY
),

-- Visit T003 - Diabetes
(
    (SELECT id FROM visits WHERE token_number = 'T003'),
    'Metformin 1000mg', '1 tablet', 'Twice daily',
    '30 days', 'Take with meals to reduce nausea', CURDATE() - INTERVAL 7 DAY
),
(
    (SELECT id FROM visits WHERE token_number = 'T003'),
    'Glibenclamide 5mg', '1 tablet', 'Once daily before breakfast',
    '30 days', 'Monitor blood sugar regularly', CURDATE() - INTERVAL 7 DAY
);

-- ============================================================
-- STEP 7: Sample Lab Reports
-- ============================================================

INSERT INTO lab_reports
(
    patient_id, hospital_id, test_type, remarks,
    file_name, file_path, file_size, file_type,
    collection_date, report_date, status
)
VALUES
(
    (SELECT id FROM patients WHERE uhid = 'MT10000001'),
    1,
    'Blood Test',
    'CBC Normal. WBC: 7200, RBC: 4.8M, Hb: 13.5, Platelets: 2.3L',
    'blood-test-MT10000001.pdf',
    '/uploads/lab-reports/blood-test-MT10000001.pdf',
    102400,
    'application/pdf',
    CURDATE() - INTERVAL 30 DAY,
    CURDATE() - INTERVAL 29 DAY,
    'COMPLETED'
),
(
    (SELECT id FROM patients WHERE uhid = 'MT10000002'),
    1,
    'ECG',
    'Normal sinus rhythm. No ST changes. PR interval normal.',
    'ecg-MT10000002.pdf',
    '/uploads/lab-reports/ecg-MT10000002.pdf',
    204800,
    'application/pdf',
    CURDATE() - INTERVAL 15 DAY,
    CURDATE() - INTERVAL 15 DAY,
    'COMPLETED'
),
(
    (SELECT id FROM patients WHERE uhid = 'MT10000003'),
    2,
    'Blood Sugar - Fasting',
    'Fasting blood glucose: 186 mg/dL. HbA1c: 8.2%. Elevated - needs dose adjustment.',
    'bsg-MT10000003.pdf',
    '/uploads/lab-reports/bsg-MT10000003.pdf',
    81920,
    'application/pdf',
    CURDATE() - INTERVAL 7 DAY,
    CURDATE() - INTERVAL 7 DAY,
    'COMPLETED'
),
(
    (SELECT id FROM patients WHERE uhid = 'MT10000003'),
    2,
    'Urine Test',
    'Urine routine normal. No proteins. No ketones.',
    'urine-MT10000003.pdf',
    '/uploads/lab-reports/urine-MT10000003.pdf',
    65536,
    'application/pdf',
    CURDATE() - INTERVAL 7 DAY,
    CURDATE() - INTERVAL 7 DAY,
    'COMPLETED'
),
(
    (SELECT id FROM patients WHERE uhid = 'MT10000004'),
    1,
    'Chest X-Ray',
    'Lung fields clear. No consolidation. Heart size normal.',
    'xray-MT10000004.pdf',
    '/uploads/lab-reports/xray-MT10000004.pdf',
    524288,
    'application/pdf',
    CURDATE() - INTERVAL 20 DAY,
    CURDATE() - INTERVAL 19 DAY,
    'COMPLETED'
);

-- ============================================================
-- STEP 8: Emergency Info
-- ============================================================

INSERT INTO emergency_info
(
    patient_id, contact_name, contact_mobile, contact_relation,
    blood_group, allergies, chronic_diseases, current_medications
)
VALUES
(
    (SELECT id FROM patients WHERE uhid = 'MT10000001'),
    'Kanna Sr.', '9876540001', 'Father',
    'B+', 'None', 'None', 'None'
),
(
    (SELECT id FROM patients WHERE uhid = 'MT10000002'),
    'Suresh Sharma', '9876540002', 'Husband',
    'O+', 'Penicillin', 'Hypertension', 'Amlodipine 5mg once daily'
),
(
    (SELECT id FROM patients WHERE uhid = 'MT10000003'),
    'Meena Murugan', '9876540003', 'Wife',
    'A+', 'Sulpha drugs', 'Type 2 Diabetes, Hypertension',
    'Metformin 500mg twice daily'
),
(
    (SELECT id FROM patients WHERE uhid = 'MT10000004'),
    'Ravi Kumar', '9876540004', 'Father',
    'AB+', 'Aspirin', 'Asthma', 'Salbutamol inhaler as needed'
),
(
    (SELECT id FROM patients WHERE uhid = 'MT10000005'),
    'Rajan', '9876540005', 'Father',
    'O-', 'None', 'None', 'None'
);