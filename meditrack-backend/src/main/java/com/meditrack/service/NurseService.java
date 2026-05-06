package com.meditrack.service;

import com.meditrack.dto.VitalsRequest;
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
public class NurseService {

    @Autowired private VitalsRepository vitalsRepository;
    @Autowired private VisitRepository visitRepository;
    @Autowired private StaffRepository staffRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private AuditLogRepository auditLogRepository;
    @Autowired private JwtUtil jwtUtil;

    public Map<String, Object> getDashboard(String token) {
        Long userId = jwtUtil.extractUserId(token.substring(7));
        Staff nurse = staffRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Nurse not found"));

        List<Visit> todayVisits = visitRepository.findByHospitalIdAndVisitDate(
                nurse.getHospital().getId(), LocalDate.now());

        long waiting = todayVisits.stream()
                .filter(v -> v.getStatus() == Visit.VisitStatus.WAITING).count();
        long withDoctor = todayVisits.stream()
                .filter(v -> v.getStatus() == Visit.VisitStatus.WITH_DOCTOR).count();
        long completed = todayVisits.stream()
                .filter(v -> v.getStatus() == Visit.VisitStatus.COMPLETED).count();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("totalToday", todayVisits.size());
        result.put("waiting", waiting);
        result.put("withDoctor", withDoctor);
        result.put("completed", completed);
        return result;
    }

    public List<Map<String, Object>> getPatientQueue(String token) {
        Long userId = jwtUtil.extractUserId(token.substring(7));
        Staff nurse = staffRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Nurse not found"));

        if (nurse.getHospital() == null) {
            return Collections.emptyList();
        }

        return visitRepository.findByHospitalIdAndStatusNotOrderByCheckInTimeAsc(
                nurse.getHospital().getId(), Visit.VisitStatus.COMPLETED)
                .stream().map(v -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("visitId", v.getId());
                    m.put("patientName", v.getPatient().getName());
                    m.put("uhid", v.getPatient().getUhid());
                    m.put("tokenNumber", v.getTokenNumber());
                    m.put("status", v.getStatus());
                    m.put("department", v.getDepartment());
                    m.put("checkInTime", v.getCheckInTime());
                    return m;
                }).toList();
    }

    @Transactional
    public Map<String, Object> addVitals(Long visitId, VitalsRequest request, String token) {
        Long userId = jwtUtil.extractUserId(token.substring(7));
        Staff nurse = staffRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Nurse not found"));

        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new ResourceNotFoundException("Visit", "id", visitId));

        Vitals vitals = Vitals.builder()
                .visit(visit)
                .nurse(nurse)
                .bloodPressureSystolic(request.getBloodPressureSystolic())
                .bloodPressureDiastolic(request.getBloodPressureDiastolic())
                .pulseRate(request.getPulseRate())
                .temperature(request.getTemperature())
                .weight(request.getWeight())
                .height(request.getHeight())
                .bmi(request.getBmi())
                .oxygenSaturation(request.getOxygenSaturation())
                .bloodSugar(request.getBloodSugar())
                .symptoms(request.getSymptoms())
                .nursingNotes(request.getNursingNotes())
                .build();

        vitalsRepository.save(vitals);

        visit.setNurse(nurse);
        visit.setStatus(Visit.VisitStatus.WITH_NURSE);
        visitRepository.save(visit);

        auditLogRepository.save(AuditLog.builder()
                .action("ADD_VITALS").actor(nurse.getName())
                .role(Role.NURSE).hospital(nurse.getHospital().getName())
                .details("Vitals recorded for visit: " + visitId).build());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", vitals.getId());
        result.put("message", "Vitals recorded successfully");
        return result;
    }

    @Transactional
    public Map<String, Object> sendToDoctor(Long visitId, String token) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new ResourceNotFoundException("Visit", "id", visitId));

        visit.setStatus(Visit.VisitStatus.WITH_DOCTOR);
        visitRepository.save(visit);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", "Patient sent to doctor queue");
        result.put("visitId", visitId);
        return result;
    }
}
