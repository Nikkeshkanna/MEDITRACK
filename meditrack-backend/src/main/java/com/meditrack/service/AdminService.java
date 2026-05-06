package com.meditrack.service;

import com.meditrack.dto.*;
import com.meditrack.entity.*;
import com.meditrack.exception.ResourceNotFoundException;
import com.meditrack.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@SuppressWarnings("null")
public class AdminService {

    @Autowired private HospitalRepository hospitalRepository;
    @Autowired private StaffRepository staffRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private DepartmentRepository departmentRepository;
    @Autowired private VisitRepository visitRepository;
    @Autowired private AuditLogRepository auditLogRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Map<String, Object> getDashboard() {
        long totalHospitals = hospitalRepository.count();
        long activeHospitals = hospitalRepository.countByActiveTrue();
        long totalPatients = patientRepository.count();
        long totalStaff = staffRepository.count();
        long todayVisits = visitRepository.countByVisitDate(LocalDate.now());
        long pendingReports = 0;

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("totalHospitals", totalHospitals);
        result.put("activeHospitals", activeHospitals);
        result.put("totalPatients", totalPatients);
        result.put("totalStaff", totalStaff);
        result.put("todayVisits", todayVisits);
        result.put("pendingReports", pendingReports);
        return result;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getHospitals() {
        return hospitalRepository.findAll().stream()
                .map(h -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("id", h.getId());
                    m.put("name", h.getName());
                    m.put("district", h.getDistrict());
                    m.put("address", h.getAddress());
                    m.put("phone", h.getPhone());
                    m.put("email", h.getEmail());
                    m.put("type", h.getType());
                    m.put("totalBeds", h.getTotalBeds());
                    m.put("registrationNumber", h.getRegistrationNumber());
                    m.put("active", h.isActive());
                    return m;
                }).toList();
    }

    @Transactional
    public Map<String, Object> addHospital(HospitalRequest request) {
        Hospital hospital = Hospital.builder()
                .name(request.getName())
                .district(request.getDistrict())
                .address(request.getAddress())
                .phone(request.getPhone())
                .email(request.getEmail())
                .registrationNumber(request.getRegistrationNumber())
                .type(request.getType() != null
                        ? Hospital.HospitalType.valueOf(request.getType().toUpperCase()) : null)
                .totalBeds(request.getTotalBeds())
                .active(true)
                .build();
        hospitalRepository.save(hospital);

        logAction("ADD_HOSPITAL", "Admin", Role.ADMIN, "System",
                "Hospital added: " + request.getName());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", hospital.getId());
        result.put("name", hospital.getName());
        result.put("message", "Hospital added successfully");
        return result;
    }

    @Transactional
    public Map<String, Object> updateHospital(Long id, HospitalRequest request) {
        Hospital hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital", "id", id));

        if (request.getName() != null) hospital.setName(request.getName());
        if (request.getDistrict() != null) hospital.setDistrict(request.getDistrict());
        if (request.getAddress() != null) hospital.setAddress(request.getAddress());
        if (request.getPhone() != null) hospital.setPhone(request.getPhone());
        if (request.getEmail() != null) hospital.setEmail(request.getEmail());
        if (request.getTotalBeds() != null) hospital.setTotalBeds(request.getTotalBeds());

        hospitalRepository.save(hospital);
        logAction("UPDATE_HOSPITAL", "Admin", Role.ADMIN, "System",
                "Hospital updated: " + hospital.getName());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", "Hospital updated successfully");
        return result;
    }

    @Transactional
    public Map<String, Object> toggleHospital(Long id) {
        Hospital hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital", "id", id));
        hospital.setActive(!hospital.isActive());
        hospitalRepository.save(hospital);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("active", hospital.isActive());
        result.put("message", "Hospital " + (hospital.isActive() ? "activated" : "deactivated"));
        return result;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getStaff(Long hospitalId) {
        List<Staff> staffList = hospitalId != null
                ? staffRepository.findByHospitalId(hospitalId)
                : staffRepository.findAll();

        return staffList.stream().map(s -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", s.getId());
            m.put("name", s.getName());
            m.put("email", s.getEmail());
            m.put("mobile", s.getMobile());
            m.put("role", s.getRole());
            m.put("department", s.getDepartment());
            m.put("qualification", s.getQualification());
            
            if (s.getHospital() != null) {
                m.put("hospitalName", s.getHospital().getName());
                m.put("hospitalId", s.getHospital().getId());
            } else {
                m.put("hospitalName", "Unknown / N/A");
                m.put("hospitalId", null);
            }
            
            m.put("active", s.isActive());
            return m;
        }).toList();
    }

    @Transactional
    public Map<String, Object> addStaff(StaffRequest request) {
        if (staffRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered for a staff member");
        }

        Hospital hospital = hospitalRepository.findById(request.getHospitalId())
                .orElseThrow(() -> new ResourceNotFoundException("Hospital", "id",
                        request.getHospitalId()));

        Role role = Role.valueOf(request.getRole().toUpperCase());

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .mobile(request.getMobile())
                .username(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .active(true)
                .build();
        userRepository.save(user);

        Staff staff = Staff.builder()
                .name(request.getName())
                .email(request.getEmail())
                .mobile(request.getMobile())
                .role(role)
                .hospital(hospital)
                .department(request.getDepartment())
                .qualification(request.getQualification())
                .registrationNumber(request.getRegistrationNumber())
                .user(user)
                .active(true)
                .build();
        staffRepository.save(staff);

        logAction("ADD_STAFF", "Admin", Role.ADMIN, hospital.getName(),
                "Staff added: " + request.getName() + " (" + role + ")");

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", staff.getId());
        result.put("name", staff.getName());
        result.put("role", staff.getRole());
        result.put("message", "Staff added successfully");
        return result;
    }

    @Transactional
    public Map<String, Object> updateStaff(Long id, StaffRequest request) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff", "id", id));

        if (request.getName() != null) {
            staff.setName(request.getName());
            if (staff.getUser() != null) staff.getUser().setName(request.getName());
        }
        if (request.getMobile() != null) staff.setMobile(request.getMobile());
        if (request.getDepartment() != null) staff.setDepartment(request.getDepartment());
        if (request.getQualification() != null) staff.setQualification(request.getQualification());

        staffRepository.save(staff);
        if (staff.getUser() != null) userRepository.save(staff.getUser());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", "Staff updated successfully");
        return result;
    }

    @Transactional
    public Map<String, Object> toggleStaff(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff", "id", id));
        staff.setActive(!staff.isActive());
        if (staff.getUser() != null) staff.getUser().setActive(staff.isActive());
        staffRepository.save(staff);
        if (staff.getUser() != null) userRepository.save(staff.getUser());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("active", staff.isActive());
        result.put("message", "Staff " + (staff.isActive() ? "activated" : "deactivated"));
        return result;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getDepartments(Long hospitalId) {
        return departmentRepository.findByHospitalId(hospitalId).stream()
                .map(d -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("id", d.getId());
                    m.put("name", d.getName());
                    m.put("headDoctor", d.getHeadDoctor());
                    m.put("floor", d.getFloor());
                    m.put("phone", d.getPhone());
                    return m;
                }).toList();
    }

    @Transactional
    public Map<String, Object> addDepartment(Map<String, Object> data) {
        Long hospitalId = Long.parseLong(data.get("hospitalId").toString());
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital", "id", hospitalId));

        Department dept = Department.builder()
                .name((String) data.get("name"))
                .headDoctor((String) data.get("headDoctor"))
                .floor((String) data.get("floor"))
                .phone((String) data.get("phone"))
                .hospital(hospital)
                .active(true)
                .build();
        departmentRepository.save(dept);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", dept.getId());
        result.put("message", "Department added successfully");
        return result;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAuditLogs() {
        return auditLogRepository.findTop100ByOrderByTimestampDesc()
                .stream().map(log -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("id", log.getId());
                    m.put("action", log.getAction());
                    m.put("actor", log.getActor());
                    m.put("role", log.getRole());
                    m.put("hospital", log.getHospital());
                    m.put("details", log.getDetails());
                    m.put("ipAddress", log.getIpAddress());
                    m.put("timestamp", log.getTimestamp());
                    return m;
                }).toList();
    }

    private void logAction(String action, String actor, Role role, String hospital, String details) {
        auditLogRepository.save(AuditLog.builder()
                .action(action).actor(actor).role(role)
                .hospital(hospital).details(details).build());
    }
}
