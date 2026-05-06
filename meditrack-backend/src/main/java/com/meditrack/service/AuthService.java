package com.meditrack.service;

import com.meditrack.dto.*;
import com.meditrack.entity.*;
import com.meditrack.repository.*;
import com.meditrack.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@SuppressWarnings("null")
public class AuthService {

    @Autowired private UserRepository userRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private StaffRepository staffRepository;
    @Autowired private HospitalRepository hospitalRepository;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private QrService qrService;
    @Autowired private AuditLogRepository auditLogRepository;

    @Transactional
    public AuthResponse patientLogin(AuthRequest request) {
        String identifier = request.getIdentifier();

        User user = userRepository.findByEmailOrMobileOrUsername(identifier, identifier, identifier)
                .orElseThrow(() -> new RuntimeException("No account found with these credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        if (!user.isActive()) {
            throw new RuntimeException("Account is deactivated. Contact support.");
        }

        if (user.getRole() != Role.PATIENT) {
            throw new RuntimeException("Please use Staff login for staff accounts");
        }

        Patient patient = patientRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Patient profile not found"));

        logAction("PATIENT_LOGIN", user.getName(), Role.PATIENT, "Patient Portal", "Patient logged in");

        String token = jwtUtil.generateToken(
                user.getEmail() != null ? user.getEmail() : user.getMobile(),
                user.getRole().name(),
                user.getName(),
                user.getId()
        );

        return AuthResponse.builder()
                .token(token)
                .role(user.getRole().name())
                .name(user.getName())
                .userId(user.getId())
                .message("Login successful")
                .build();
    }

    @Transactional
    public AuthResponse patientRegister(PatientRegisterRequest request) {
        if (userRepository.existsByMobile(request.getMobile())) {
            throw new IllegalArgumentException("Mobile number already registered");
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()
                && userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = User.builder()
                .name(request.getName())
                .mobile(request.getMobile())
                .email(request.getEmail())
                .username(request.getMobile())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.PATIENT)
                .active(true)
                .build();
        userRepository.save(user);

        String uhid = generateUHID();

        Patient patient = Patient.builder()
                .uhid(uhid)
                .name(request.getName())
                .mobile(request.getMobile())
                .email(request.getEmail())
                .dob(request.getDob() != null ? LocalDate.parse(request.getDob()) : null)
                .gender(request.getGender())
                .bloodGroup(request.getBloodGroup())
                .address(request.getAddress())
                .allergies(request.getAllergies())
                .chronicDiseases(request.getChronicDiseases())
                .currentMedications(request.getCurrentMedications())
                .emergencyContactName(request.getEmergencyContactName())
                .emergencyContactMobile(request.getEmergencyContactMobile())
                .emergencyContactRelation(request.getEmergencyContactRelation())
                .user(user)
                .build();

        patientRepository.save(patient);

        try {
            String qrPath = qrService.saveQRCode(patient);
            patient.setQrCodeData(qrPath);
            patientRepository.save(patient);
        } catch (Exception ignored) {}

        logAction("PATIENT_REGISTER", request.getName(), Role.PATIENT,
                "Patient Portal", "New patient registered. UHID: " + uhid);

        return AuthResponse.builder()
                .message("Registration successful! Your UHID is: " + uhid)
                .build();
    }

    @Transactional
    public AuthResponse staffLogin(AuthRequest request) {
        String identifier = request.getIdentifier();

        User user = userRepository.findByEmailOrMobileOrUsername(identifier, identifier, identifier)
                .orElseThrow(() -> new RuntimeException("No staff account found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        if (!user.isActive()) {
            throw new RuntimeException("Account deactivated. Contact admin.");
        }

        if (user.getRole() == Role.PATIENT || user.getRole() == Role.ADMIN) {
            throw new RuntimeException("Please use correct login portal");
        }

        if (request.getHospitalId() != null) {
            Staff staff = staffRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Staff profile not found"));
            if (!staff.getHospital().getId().equals(request.getHospitalId())) {
                throw new RuntimeException("Staff does not belong to selected hospital");
            }
        }

        logAction("STAFF_LOGIN", user.getName(), user.getRole(),
                "Hospital", "Staff login: " + user.getRole());

        String token = jwtUtil.generateToken(
                user.getEmail() != null ? user.getEmail() : user.getMobile(),
                user.getRole().name(),
                user.getName(),
                user.getId()
        );

        return AuthResponse.builder()
                .token(token)
                .role(user.getRole().name())
                .name(user.getName())
                .userId(user.getId())
                .message("Login successful")
                .build();
    }

    @Transactional
    public AuthResponse adminLogin(AuthRequest request) {
        String identifier = request.getIdentifier();

        User user = userRepository.findByEmailOrMobileOrUsername(identifier, identifier, identifier)
                .orElseThrow(() -> new RuntimeException("Admin account not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid admin credentials");
        }

        if (user.getRole() != Role.ADMIN) {
            throw new RuntimeException("Not an admin account");
        }

        logAction("ADMIN_LOGIN", user.getName(), Role.ADMIN, "System", "Admin logged in");

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name(),
                user.getName(),
                user.getId()
        );

        return AuthResponse.builder()
                .token(token)
                .role(Role.ADMIN.name())
                .name(user.getName())
                .userId(user.getId())
                .message("Admin login successful")
                .build();
    }

    public List<Map<String, Object>> getHospitalList() {
        return hospitalRepository.findByActiveTrue().stream()
                .map(h -> {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("id", h.getId());
                    map.put("name", h.getName());
                    map.put("district", h.getDistrict());
                    map.put("type", h.getType());
                    return map;
                }).toList();
    }

    private String generateUHID() {
        String prefix = "MT";
        String timestamp = String.valueOf(System.currentTimeMillis()).substring(5);
        String uhid = prefix + timestamp;
        while (patientRepository.existsByUhid(uhid)) {
            uhid = prefix + (System.currentTimeMillis() % 100000000);
        }
        return uhid;
    }

    private void logAction(String action, String actor, Role role, String hospital, String details) {
        AuditLog log = AuditLog.builder()
                .action(action)
                .actor(actor)
                .role(role)
                .hospital(hospital)
                .details(details)
                .build();
        auditLogRepository.save(log);
    }
}
