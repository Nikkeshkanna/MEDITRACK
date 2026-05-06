package com.meditrack.service;

import com.meditrack.entity.*;
import com.meditrack.exception.ResourceNotFoundException;
import com.meditrack.repository.*;
import com.meditrack.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@SuppressWarnings("null")
public class ReceptionService {

    @Autowired private VisitRepository visitRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private StaffRepository staffRepository;
    @Autowired private HospitalRepository hospitalRepository;
    @Autowired private AuditLogRepository auditLogRepository;
    @Autowired private AuthService authService;
    @Autowired private JwtUtil jwtUtil;

    @Transactional
    public List<Map<String, Object>> searchPatient(String query, String token) {
        return patientRepository.searchPatients(query).stream()
                .map(p -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("id", p.getId());
                    m.put("uhid", p.getUhid());
                    m.put("name", p.getName());
                    m.put("mobile", p.getMobile());
                    m.put("dob", p.getDob());
                    m.put("gender", p.getGender());
                    m.put("bloodGroup", p.getBloodGroup());
                    return m;
                }).toList();
    }

    @Transactional
    public Map<String, Object> registerPatient(com.meditrack.dto.PatientRegisterRequest data, String token) {
        // First register the patient using AuthService
        com.meditrack.dto.AuthResponse authResponse = authService.patientRegister(data);
        
        // Extract UHID from message if needed, but better to find the patient by mobile
        Patient patient = patientRepository.findByMobile(data.getMobile())
                .orElseThrow(() -> new RuntimeException("Patient registration failed"));

        // Now return patient info for the check-in flow
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", patient.getId());
        result.put("uhid", patient.getUhid());
        result.put("name", patient.getName());
        result.put("message", authResponse.getMessage());
        return result;
    }

    @Transactional
    public Map<String, Object> checkInPatient(Map<String, Object> data, String token) {
        try {
            Long userId = jwtUtil.extractUserId(token.substring(7));
            Staff receptionist = staffRepository.findByUserId(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Receptionist not found"));

            Patient patient;
            if (data.get("patientId") != null) {
                Long patientId = Long.parseLong(data.get("patientId").toString());
                patient = patientRepository.findById(patientId)
                        .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", patientId));
            } else if (data.get("uhid") != null) {
                String uhid = data.get("uhid").toString();
                patient = patientRepository.findByUhid(uhid)
                        .orElseThrow(() -> new ResourceNotFoundException("Patient not found with UHID: " + uhid));
            } else {
                throw new IllegalArgumentException("Patient ID or UHID is missing");
            }

            String department = (String) data.get("department");
            String visitTypeStr = (String) data.getOrDefault("visitType", "OPD");

            String tokenNumber = generateToken(receptionist.getHospital().getId());

            Visit visit = Visit.builder()
                    .patient(patient)
                    .hospital(receptionist.getHospital())
                    .department(department)
                    .visitType(Visit.VisitType.valueOf(visitTypeStr.toUpperCase().replace("-", "_")))
                    .status(Visit.VisitStatus.WAITING)
                    .visitDate(LocalDate.now())
                    .checkInTime(LocalDateTime.now())
                    .tokenNumber(tokenNumber)
                    .chiefComplaint((String) data.get("notes"))
                    .build();

            visitRepository.save(visit);

            auditLogRepository.save(AuditLog.builder()
                    .action("PATIENT_CHECKIN").actor(receptionist.getName())
                    .role(Role.RECEPTIONIST).hospital(receptionist.getHospital().getName())
                    .details("Patient checked in: " + patient.getUhid()
                            + " Token: " + tokenNumber).build());

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("visitId", visit.getId());
            result.put("tokenNumber", tokenNumber);
            result.put("department", department);
            result.put("patientName", patient.getName());
            result.put("uhid", patient.getUhid());
            result.put("checkInTime", visit.getCheckInTime());
            result.put("message", "Check-in successful");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Map<String, Object> getDashboard(String token) {
        Long userId = jwtUtil.extractUserId(token.substring(7));
        Staff receptionist = staffRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Receptionist not found"));

        long hospitalId = receptionist.getHospital().getId();
        List<Visit> todayVisits = visitRepository.findByHospitalIdAndVisitDate(
                hospitalId, LocalDate.now());

        long waiting = todayVisits.stream()
                .filter(v -> v.getStatus() == Visit.VisitStatus.WAITING).count();

        List<Map<String, Object>> recentCheckins = todayVisits.stream()
                .sorted(Comparator.comparing(Visit::getCheckInTime,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(10)
                .map(v -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("visitId", v.getId());
                    m.put("patientName", v.getPatient().getName());
                    m.put("uhid", v.getPatient().getUhid());
                    m.put("tokenNumber", v.getTokenNumber());
                    m.put("department", v.getDepartment());
                    m.put("checkInTime", v.getCheckInTime());
                    m.put("status", v.getStatus());
                    return m;
                }).toList();

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("todayVisits", todayVisits.size());
        stats.put("waiting", waiting);
        stats.put("newRegistrations", 0);
        stats.put("appointments", 0);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("stats", stats);
        result.put("recentCheckins", recentCheckins);
        return result;
    }

    private String generateToken(Long hospitalId) {
        long count = visitRepository.countTodayVisits(hospitalId, LocalDate.now());
        return "T" + String.format("%03d", count + 1);
    }
}
