package com.meditrack.service;
// Re-compile trigger

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
public class LabService {

    @Autowired private LabReportRepository labReportRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private VisitRepository visitRepository;
    @Autowired private StaffRepository staffRepository;
    @Autowired private AuditLogRepository auditLogRepository;
    @Autowired private JwtUtil jwtUtil;

    @Value("${meditrack.upload.dir}")
    private String uploadDir;

    @Transactional
    public Map<String, Object> uploadLabReport(MultipartFile file, Long patientId,
                                                Long visitId, String testType,
                                                String remarks, String collectionDate,
                                                String token) throws IOException {
        Long userId = jwtUtil.extractUserId(token.substring(7));
        Staff labTech = staffRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Lab technician not found"));

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", patientId));

        Path labDir = Paths.get(uploadDir, "lab-reports");
        Files.createDirectories(labDir);
        String fileName = "lab-" + System.currentTimeMillis() + "-"
                + file.getOriginalFilename().replaceAll("\\s+", "_");
        Path filePath = labDir.resolve(fileName);
        Files.write(filePath, file.getBytes());

        LabReport.LabReportBuilder builder = LabReport.builder()
                .patient(patient)
                .labTechnician(labTech)
                .hospital(labTech.getHospital())
                .testType(testType)
                .remarks(remarks)
                .fileName(fileName)
                .filePath("/uploads/lab-reports/" + fileName)
                .fileSize(file.getSize())
                .fileType(file.getContentType())
                .reportDate(LocalDate.now())
                .status(LabReport.ReportStatus.COMPLETED);

        if (collectionDate != null && !collectionDate.isBlank()) {
            builder.collectionDate(LocalDate.parse(collectionDate));
        }
        if (visitId != null) {
            visitRepository.findById(visitId).ifPresent(builder::visit);
        }

        LabReport report = labReportRepository.save(builder.build());

        auditLogRepository.save(AuditLog.builder()
                .action("UPLOAD_REPORT").actor(labTech.getName())
                .role(Role.LAB_TECHNICIAN).hospital(labTech.getHospital().getName())
                .details("Lab report uploaded: " + testType + " for patient: "
                        + patient.getUhid()).build());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", report.getId());
        result.put("message", "Report uploaded successfully");
        result.put("filePath", report.getFilePath());
        return result;
    }

    public List<Map<String, Object>> getPendingTests(String token) {
        Long userId = jwtUtil.extractUserId(token.substring(7));
        Staff labTech = staffRepository.findByUserId(userId)
                .orElseThrow(() -> new com.meditrack.exception.ResourceNotFoundException("Lab staff not found"));

        return labReportRepository
                .findByHospitalIdAndStatus(labTech.getHospital().getId(),
                        LabReport.ReportStatus.PENDING)
                .stream().map(r -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("id", r.getId());
                    m.put("patientName", r.getPatient().getName());
                    m.put("uhid", r.getPatient().getUhid());
                    m.put("testType", r.getTestType());
                    m.put("orderedDate", r.getCreatedAt());
                    m.put("doctorName", r.getVisit() != null && r.getVisit().getDoctor() != null
                            ? r.getVisit().getDoctor().getName() : "N/A");
                    m.put("status", r.getStatus());
                    return m;
                }).toList();
    }

    @Transactional
    public Map<String, Object> updateTestStatus(Long id, String status) {
        LabReport report = labReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report", "id", id));
        report.setStatus(LabReport.ReportStatus.valueOf(status));
        labReportRepository.save(report);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", "Status updated to " + status);
        return result;
    }

    public Map<String, Object> getDashboard(String token) {
        Long userId = jwtUtil.extractUserId(token.substring(7));
        Staff labTech = staffRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Lab staff not found"));

        long pending = labReportRepository.countByStatus(LabReport.ReportStatus.PENDING);
        long inProgress = labReportRepository.countByStatus(LabReport.ReportStatus.IN_PROGRESS);
        long completed = labReportRepository.countByStatus(LabReport.ReportStatus.COMPLETED);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("pending", pending);
        result.put("inProgress", inProgress);
        result.put("completed", completed);
        result.put("totalToday", pending + inProgress + completed);
        return result;
    }

    public byte[] downloadReport(Long id) throws IOException {
        LabReport report = labReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report", "id", id));
        Path filePath = Paths.get(uploadDir, "lab-reports", report.getFileName());
        return Files.readAllBytes(filePath);
    }
}
