package com.meditrack.service;

import com.meditrack.dto.AppointmentRequest;
import com.meditrack.entity.*;
import com.meditrack.exception.ResourceNotFoundException;
import com.meditrack.repository.*;
import com.meditrack.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
@SuppressWarnings("null")
public class AppointmentService {

    @Autowired private AppointmentRepository appointmentRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private StaffRepository staffRepository;
    @Autowired private HospitalRepository hospitalRepository;
    @Autowired private AuditLogRepository auditLogRepository;
    @Autowired private JwtUtil jwtUtil;

    @Transactional
    public Map<String, Object> bookAppointment(AppointmentRequest request, String token) {
        Long userId = jwtUtil.extractUserId(token.substring(7));
        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        Hospital hospital = hospitalRepository.findById(request.getHospitalId())
                .orElseThrow(() -> new ResourceNotFoundException("Hospital", "id", request.getHospitalId()));

        Staff doctor = null;
        if (request.getDoctorId() != null) {
            doctor = staffRepository.findById(request.getDoctorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", request.getDoctorId()));
        }

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .hospital(hospital)
                .doctor(doctor)
                .department(request.getDepartment())
                .appointmentDate(LocalDate.parse(request.getDate()))
                .appointmentTime(LocalTime.parse(request.getTime()))
                .reason(request.getReason())
                .notes(request.getNotes())
                .status(Appointment.AppointmentStatus.PENDING)
                .build();

        appointmentRepository.save(appointment);

        auditLogRepository.save(AuditLog.builder()
                .action("BOOK_APPOINTMENT").actor(patient.getName())
                .role(Role.PATIENT).hospital(hospital.getName())
                .details("Appointment booked for: " + appointment.getAppointmentDate()).build());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", appointment.getId());
        result.put("status", appointment.getStatus());
        result.put("message", "Appointment booked successfully");
        return result;
    }

    public List<Map<String, Object>> getPatientAppointments(String token) {
        Long userId = jwtUtil.extractUserId(token.substring(7));
        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        return appointmentRepository.findByPatientIdOrderByAppointmentDateDesc(patient.getId())
                .stream().map(this::buildAppointmentMap).toList();
    }

    public List<Map<String, Object>> getDoctorAppointments(String token) {
        Long userId = jwtUtil.extractUserId(token.substring(7));
        Staff doctor = staffRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        return appointmentRepository.findByDoctorIdAndAppointmentDate(doctor.getId(), LocalDate.now())
                .stream().map(this::buildAppointmentMap).toList();
    }

    @Transactional
    public Map<String, Object> updateStatus(Long id, String status, String token) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", id));
        
        appointment.setStatus(Appointment.AppointmentStatus.valueOf(status.toUpperCase()));
        appointmentRepository.save(appointment);

        return Collections.singletonMap("message", "Appointment status updated to " + status);
    }

    private Map<String, Object> buildAppointmentMap(Appointment a) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", a.getId());
        m.put("date", a.getAppointmentDate());
        m.put("time", a.getAppointmentTime());
        m.put("status", a.getStatus());
        m.put("hospitalName", a.getHospital().getName());
        m.put("doctorName", a.getDoctor() != null ? a.getDoctor().getName() : "Any Doctor");
        m.put("department", a.getDepartment());
        m.put("reason", a.getReason());
        return m;
    }
}
