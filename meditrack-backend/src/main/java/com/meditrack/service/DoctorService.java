package com.meditrack.service;

import com.meditrack.dto.*;
import com.meditrack.entity.*;
import com.meditrack.exception.ResourceNotFoundException;
import com.meditrack.repository.*;
import com.meditrack.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@SuppressWarnings("null")
public class DoctorService {

    @Autowired private PatientRepository patientRepository;
    @Autowired private VisitRepository visitRepository;
    @Autowired private DiagnosisRepository diagnosisRepository;
    @Autowired private PrescriptionRepository prescriptionRepository;
    @Autowired private StaffRepository staffRepository;
    @Autowired private AuditLogRepository auditLogRepository;
    @Autowired private JwtUtil jwtUtil;

    public List<Map<String, Object>> searchPatient(String query) {
        return patientRepository.searchPatients(query).stream()
                .map(this::buildPatientSummary).toList();
    }

    public Map<String, Object> getPatientByUHID(String uhid) {
        Patient patient = patientRepository.findByUhid(uhid)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "UHID", uhid));
        return buildPatientSummary(patient);
    }

    public List<Map<String, Object>> getPatientHistory(String uhid) {
        Patient patient = patientRepository.findByUhid(uhid)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "UHID", uhid));
        return visitRepository.findByPatientIdOrderByVisitDateDesc(patient.getId())
                .stream().map(this::buildVisitDetail).toList();
    }

    public List<Map<String, Object>> getTodayPatients(String token) {
        Long userId = jwtUtil.extractUserId(token.substring(7));
        Staff doctor = staffRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));

        if (doctor.getHospital() == null) {
            return Collections.emptyList();
        }

        // Get visits assigned to this doctor OR currently WAITING in this doctor's department
        Set<Visit> visitSet = new HashSet<>();
        
        // 1. Assigned to me
        visitSet.addAll(visitRepository.findByDoctorIdAndVisitDate(doctor.getId(), LocalDate.now()));
        
        // 2. Waiting in my department
        visitRepository.findByHospitalIdAndDepartmentAndVisitDate(
                doctor.getHospital().getId(), doctor.getDepartment(), LocalDate.now())
                .stream()
                .filter(v -> v.getStatus() == Visit.VisitStatus.WAITING)
                .forEach(visitSet::add);

        List<Visit> visits = new ArrayList<>(visitSet);
        visits.sort((v1, v2) -> {
            if (v1.getCheckInTime() == null && v2.getCheckInTime() == null) return 0;
            if (v1.getCheckInTime() == null) return 1;
            if (v2.getCheckInTime() == null) return -1;
            return v1.getCheckInTime().compareTo(v2.getCheckInTime());
        });

        return visits.stream().map(v -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("visitId", v.getId());
            m.put("patientName", v.getPatient().getName());
            m.put("uhid", v.getPatient().getUhid());
            m.put("status", v.getStatus());
            m.put("tokenNumber", v.getTokenNumber());
            m.put("department", v.getDepartment());
            m.put("checkInTime", v.getCheckInTime());
            return m;
        }).toList();
    }

    @Transactional
    public Map<String, Object> addDiagnosis(Long visitId, DiagnosisRequest request, String token) {
        Long userId = jwtUtil.extractUserId(token.substring(7));
        Staff doctor = staffRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new ResourceNotFoundException("Visit", "id", visitId));

        Diagnosis diagnosis = Diagnosis.builder()
                .visit(visit)
                .doctor(doctor)
                .primaryDiagnosis(request.getPrimaryDiagnosis())
                .secondaryDiagnosis(request.getSecondaryDiagnosis())
                .symptoms(request.getSymptoms())
                .clinicalNotes(request.getClinicalNotes())
                .followUpRequired(request.isFollowUpRequired())
                .followUpDate(request.getFollowUpDate() != null
                        ? LocalDate.parse(request.getFollowUpDate()) : null)
                .followUpNotes(request.getFollowUpNotes())
                .build();

        diagnosisRepository.save(diagnosis);

        visit.setDoctor(doctor);
        visit.setStatus(Visit.VisitStatus.WITH_DOCTOR);
        visitRepository.save(visit);

        logAction("ADD_DIAGNOSIS", doctor.getName(), Role.DOCTOR,
                doctor.getHospital().getName(),
                "Diagnosis added for visit: " + visitId);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", diagnosis.getId());
        result.put("primaryDiagnosis", diagnosis.getPrimaryDiagnosis());
        result.put("message", "Diagnosis saved successfully");
        return result;
    }

    @Transactional
    public Map<String, Object> addPrescription(Long visitId, PrescriptionRequest request, String token) {
        Long userId = jwtUtil.extractUserId(token.substring(7));
        Staff doctor = staffRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new ResourceNotFoundException("Visit", "id", visitId));

        List<Prescription> saved = new ArrayList<>();
        if (request.getMedicines() != null) {
            for (PrescriptionRequest.MedicineItem med : request.getMedicines()) {
                Prescription prescription = Prescription.builder()
                        .visit(visit)
                        .doctor(doctor)
                        .medicineName(med.getName())
                        .dosage(med.getDosage())
                        .frequency(med.getFrequency())
                        .duration(med.getDuration())
                        .instructions(med.getInstructions())
                        .notes(request.getNotes())
                        .prescriptionDate(LocalDate.now())
                        .build();
                saved.add(prescriptionRepository.save(prescription));
            }
        }

        visit.setStatus(Visit.VisitStatus.COMPLETED);
        visitRepository.save(visit);

        logAction("ADD_PRESCRIPTION", doctor.getName(), Role.DOCTOR,
                doctor.getHospital().getName(),
                "Prescription added: " + saved.size() + " medicines for visit: " + visitId);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("count", saved.size());
        result.put("message", "Prescription saved successfully");
        return result;
    }

    public Map<String, Object> getDashboard(String token) {
        Long userId = jwtUtil.extractUserId(token.substring(7));
        Staff doctor = staffRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        long todayCount = visitRepository.findByDoctorIdAndVisitDate(
                doctor.getId(), LocalDate.now()).size();
        long totalPatients = patientRepository.count();
        long pendingDiagnosis = visitRepository.findByDoctorIdAndVisitDate(
                doctor.getId(), LocalDate.now())
                .stream().filter(v -> v.getStatus() == Visit.VisitStatus.WITH_DOCTOR).count();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("todayCount", todayCount);
        result.put("totalPatients", totalPatients);
        result.put("pendingDiagnosis", pendingDiagnosis);
        result.put("doctorName", doctor.getName());
        result.put("department", doctor.getDepartment());
        result.put("hospital", doctor.getHospital().getName());
        return result;
    }

    private Map<String, Object> buildPatientSummary(Patient p) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", p.getId());
        m.put("uhid", p.getUhid());
        m.put("name", p.getName());
        m.put("mobile", p.getMobile());
        m.put("dob", p.getDob());
        m.put("gender", p.getGender());
        m.put("bloodGroup", p.getBloodGroup());
        m.put("allergies", p.getAllergies());
        m.put("chronicDiseases", p.getChronicDiseases());
        m.put("emergencyContactName", p.getEmergencyContactName());
        m.put("emergencyContactMobile", p.getEmergencyContactMobile());
        m.put("emergencyContactRelation", p.getEmergencyContactRelation());

        Optional<Visit> activeVisit = visitRepository.findActiveVisitByPatient(p.getId());
        activeVisit.ifPresent(v -> m.put("activeVisitId", v.getId()));
        return m;
    }

    private Map<String, Object> buildVisitDetail(Visit v) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", v.getId());
        m.put("visitDate", v.getVisitDate());
        m.put("hospitalName", v.getHospital() != null ? v.getHospital().getName() : "");
        m.put("doctorName", v.getDoctor() != null ? v.getDoctor().getName() : "");
        m.put("department", v.getDepartment());
        m.put("type", v.getVisitType());
        m.put("status", v.getStatus());
        if (v.getDiagnosis() != null) {
            m.put("diagnosis", v.getDiagnosis().getPrimaryDiagnosis());
            m.put("notes", v.getDiagnosis().getClinicalNotes());
        }
        return m;
    }

    private void logAction(String action, String actor, Role role, String hospital, String details) {
        auditLogRepository.save(AuditLog.builder()
                .action(action).actor(actor).role(role)
                .hospital(hospital).details(details).build());
    }
}
