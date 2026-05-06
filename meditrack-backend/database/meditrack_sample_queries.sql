-- ============================================================
-- MediTrack Sample Queries for Testing
-- ============================================================

USE meditrack_db;

-- 1. Get patient profile with UHID
SELECT p.uhid, u.name, u.email, p.blood_group, p.mobile, p.allergies
FROM patients p JOIN users u ON p.user_id = u.id
WHERE p.uhid = 'MDT20260001';

-- 2. Get hospital-wise visit history for a patient
SELECT h.name AS hospital, vh.visit_date, vh.department,
       vh.diagnosis_summary, d_user.name AS doctor
FROM visit_history vh
JOIN hospitals h ON vh.hospital_id = h.id
LEFT JOIN doctors d ON vh.doctor_id = d.id
LEFT JOIN users d_user ON d.user_id = d_user.id
WHERE vh.patient_id = 1
ORDER BY vh.visit_date DESC;

-- 3. Get active prescriptions for a patient
SELECT medicine_name, dosage, frequency, duration, route, instructions, status
FROM prescriptions
WHERE patient_id = 1 AND status = 'ACTIVE'
ORDER BY prescribed_date DESC;

-- 4. Get all lab reports for a patient
SELECT report_name, report_type, lab_name, report_date, status, remarks
FROM lab_reports
WHERE patient_id = 1
ORDER BY report_date DESC;

-- 5. Get today's OPD queue for a hospital
SELECT p.uhid, u.name, o.token_number, o.priority, o.department,
       o.symptoms, o.status
FROM opd_records o
JOIN patients p ON o.patient_id = p.id
JOIN users u ON p.user_id = u.id
WHERE o.hospital_id = 1 AND o.appointment_date = CURDATE()
ORDER BY o.token_number;

-- 6. Get SMS notification history for a patient
SELECT hospital_name, doctor_name, visit_date, sms_status, sent_at
FROM notification_logs
WHERE patient_id = 1
ORDER BY created_at DESC;

-- 7. Get latest vitals for a patient
SELECT blood_pressure, blood_sugar, pulse, temperature, weight, recorded_at
FROM vitals
WHERE patient_id = 1
ORDER BY recorded_at DESC LIMIT 1;

-- 8. Emergency data lookup by UHID
SELECT u.name, p.blood_group, p.allergies, p.current_medications,
       p.emergency_contact_name, p.emergency_contact_number
FROM patients p JOIN users u ON p.user_id = u.id
WHERE p.uhid = 'MDT20260001';

-- 9. Doctor's patient count by hospital
SELECT h.name, COUNT(DISTINCT o.patient_id) AS total_patients
FROM opd_records o
JOIN hospitals h ON o.hospital_id = h.id
WHERE o.appointment_date = CURDATE()
GROUP BY h.name;

-- 10. Hospital-wise visit count per patient
SELECT h.name AS hospital, COUNT(vh.id) AS visit_count,
       MAX(vh.visit_date) AS last_visit
FROM visit_history vh
JOIN hospitals h ON vh.hospital_id = h.id
WHERE vh.patient_id = 1
GROUP BY h.id ORDER BY last_visit DESC;