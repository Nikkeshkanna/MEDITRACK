package com.meditrack.service;

import com.meditrack.entity.*;
import com.meditrack.exception.ResourceNotFoundException;
import com.meditrack.repository.*;
import com.meditrack.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

@Service
@SuppressWarnings("null")
public class PatientService {

    @Autowired private PatientRepository patientRepository;
    @Autowired private VisitRepository visitRepository;
    @Autowired private PrescriptionRepository prescriptionRepository;
    @Autowired private LabReportRepository labReportRepository;
    @Autowired private ScanReportRepository scanReportRepository;
    @Autowired private HospitalRepository hospitalRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private JwtUtil jwtUtil;

    @Value("${meditrack.upload.dir}")
    private String uploadDir;

    public Map<String, Object> getProfile(String token) {
        Long userId = jwtUtil.extractUserId(token.substring(7));
        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));
        return buildPatientMap(patient);
    }

    @Transactional
    public Map<String, Object> updateProfile(String token, Map<String, Object> data) {
        Long userId = jwtUtil.extractUserId(token.substring(7));
        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        if (data.containsKey("name")) patient.setName((String) data.get("name"));
        if (data.containsKey("email")) patient.setEmail((String) data.get("email"));
        if (data.containsKey("gender")) patient.setGender((String) data.get("gender"));
        if (data.containsKey("bloodGroup")) patient.setBloodGroup((String) data.get("bloodGroup"));
        if (data.containsKey("address")) patient.setAddress((String) data.get("address"));
        if (data.containsKey("allergies")) patient.setAllergies((String) data.get("allergies"));
        if (data.containsKey("chronicDiseases")) patient.setChronicDiseases((String) data.get("chronicDiseases"));
        if (data.containsKey("emergencyContactName")) patient.setEmergencyContactName((String) data.get("emergencyContactName"));
        if (data.containsKey("emergencyContactMobile")) patient.setEmergencyContactMobile((String) data.get("emergencyContactMobile"));
        if (data.containsKey("emergencyContactRelation")) patient.setEmergencyContactRelation((String) data.get("emergencyContactRelation"));
        if (data.containsKey("dob")) patient.setDob(LocalDate.parse((String) data.get("dob")));

        patientRepository.save(patient);
        return buildPatientMap(patient);
    }

    @Transactional
    public Map<String, Object> uploadProfilePhoto(String token, MultipartFile file) throws IOException {
        Long userId = jwtUtil.extractUserId(token.substring(7));
        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        Path photoDir = Paths.get(uploadDir, "photos");
        Files.createDirectories(photoDir);
        String fileName = patient.getUhid() + "-photo." +
                getFileExtension(file.getOriginalFilename());
        Path filePath = photoDir.resolve(fileName);
        Files.write(filePath, file.getBytes());

        patient.setPhotoUrl("/uploads/photos/" + fileName);
        patientRepository.save(patient);

        Map<String, Object> result = new HashMap<>();
        result.put("photoUrl", patient.getPhotoUrl());
        return result;
    }

    public List<Map<String, Object>> getMedicalHistory(String token) {
        Long userId = jwtUtil.extractUserId(token.substring(7));
        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        return visitRepository.findByPatientIdOrderByVisitDateDesc(patient.getId())
                .stream().map(this::buildVisitMap).toList();
    }

    public List<Map<String, Object>> getVisits(String token) {
        return getMedicalHistory(token);
    }

    public List<Map<String, Object>> getPrescriptions(String token) {
        Long userId = jwtUtil.extractUserId(token.substring(7));
        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        return prescriptionRepository.findByVisitPatientIdOrderByCreatedAtDesc(patient.getId())
                .stream().map(this::buildPrescriptionMap).toList();
    }

    public List<Map<String, Object>> getLabReports(String token) {
        Long userId = jwtUtil.extractUserId(token.substring(7));
        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        return labReportRepository.findByPatientIdOrderByCreatedAtDesc(patient.getId())
                .stream().map(this::buildReportMap).toList();
    }

    public List<Map<String, Object>> getScanReports(String token) {
        Long userId = jwtUtil.extractUserId(token.substring(7));
        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        return scanReportRepository.findByPatientIdOrderByCreatedAtDesc(patient.getId())
                .stream().map(scan -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("id", scan.getId());
                    m.put("testType", scan.getScanType());
                    m.put("reportDate", scan.getScanDate());
                    m.put("remarks", scan.getFindings());
                    m.put("fileName", scan.getFileName());
                    m.put("fileSize", scan.getFileSize());
                    m.put("status", scan.getStatus());
                    return m;
                }).toList();
    }

    public Map<String, Object> getEmergencyInfo(String token) {
        Long userId = jwtUtil.extractUserId(token.substring(7));
        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        return buildPatientMap(patient);
    }

    public Map<String, Object> getQRCode(String token) {
        Long userId = jwtUtil.extractUserId(token.substring(7));
        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("uhid", patient.getUhid());
        result.put("name", patient.getName());
        result.put("bloodGroup", patient.getBloodGroup());
        result.put("qrCodePath", patient.getQrCodeData());
        return result;
    }

    public List<Map<String, Object>> searchHospitals(String query) {
        List<Hospital> hospitals;
        if (query == null || query.trim().isEmpty()) {
            hospitals = hospitalRepository.findByActiveTrue();
        } else {
            hospitals = hospitalRepository.searchHospitals(query);
        }
        return hospitals.stream().map(h -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", h.getId());
            m.put("name", h.getName());
            m.put("district", h.getDistrict());
            m.put("type", h.getType());
            return m;
        }).toList();
    }

    private Map<String, Object> buildPatientMap(Patient p) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", p.getId());
        map.put("uhid", p.getUhid());
        map.put("name", p.getName());
        map.put("mobile", p.getMobile());
        map.put("email", p.getEmail());
        map.put("dob", p.getDob());
        map.put("gender", p.getGender());
        map.put("bloodGroup", p.getBloodGroup());
        map.put("address", p.getAddress());
        map.put("allergies", p.getAllergies());
        map.put("chronicDiseases", p.getChronicDiseases());
        map.put("currentMedications", p.getCurrentMedications());
        map.put("emergencyContactName", p.getEmergencyContactName());
        map.put("emergencyContactMobile", p.getEmergencyContactMobile());
        map.put("emergencyContactRelation", p.getEmergencyContactRelation());
        map.put("photoUrl", p.getPhotoUrl());
        map.put("qrCodeData", p.getQrCodeData());
        return map;
    }

    private Map<String, Object> buildVisitMap(Visit v) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", v.getId());
        map.put("visitDate", v.getVisitDate());
        map.put("hospitalName", v.getHospital() != null ? v.getHospital().getName() : "");
        map.put("doctorName", v.getDoctor() != null ? v.getDoctor().getName() : "");
        map.put("department", v.getDepartment());
        map.put("type", v.getVisitType());
        map.put("status", v.getStatus());
        map.put("tokenNumber", v.getTokenNumber());
        if (v.getDiagnosis() != null) {
            map.put("diagnosis", v.getDiagnosis().getPrimaryDiagnosis());
            map.put("notes", v.getDiagnosis().getClinicalNotes());
            map.put("followUp", v.getDiagnosis().isFollowUpRequired());
            map.put("followUpDate", v.getDiagnosis().getFollowUpDate());
        }
        return map;
    }

    private Map<String, Object> buildPrescriptionMap(Prescription p) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", p.getId());
        map.put("prescriptionDate", p.getPrescriptionDate());
        map.put("doctorName", p.getDoctor() != null ? p.getDoctor().getName() : "");
        map.put("hospitalName", p.getVisit() != null && p.getVisit().getHospital() != null
                ? p.getVisit().getHospital().getName() : "");
        map.put("medicineName", p.getMedicineName());
        map.put("dosage", p.getDosage());
        map.put("frequency", p.getFrequency());
        map.put("duration", p.getDuration());
        map.put("instructions", p.getInstructions());
        map.put("notes", p.getNotes());
        return map;
    }

    private Map<String, Object> buildReportMap(LabReport r) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", r.getId());
        map.put("testType", r.getTestType());
        map.put("reportDate", r.getReportDate());
        map.put("labName", r.getHospital() != null ? r.getHospital().getName() : "");
        map.put("remarks", r.getRemarks());
        map.put("fileName", r.getFileName());
        map.put("fileSize", r.getFileSize());
        map.put("status", r.getStatus());
        return map;
    }

    private String getFileExtension(String filename) {
        if (filename == null) return "jpg";
        int dotIndex = filename.lastIndexOf('.');
        return dotIndex > 0 ? filename.substring(dotIndex + 1) : "jpg";
    }
}
