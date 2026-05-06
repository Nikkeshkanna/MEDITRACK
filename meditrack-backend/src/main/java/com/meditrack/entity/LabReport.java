package com.meditrack.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "lab_reports")
@EntityListeners(AuditingEntityListener.class)
public class LabReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visit_id")
    private Visit visit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_tech_id")
    private Staff labTechnician;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @Column(nullable = false)
    private String testType;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    private String fileName;
    private String filePath;
    private Long fileSize;
    private String fileType;
    private LocalDate collectionDate;
    private LocalDate reportDate;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public enum ReportStatus {
        PENDING, IN_PROGRESS, COMPLETED
    }

    public LabReport() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Visit getVisit() { return visit; }
    public void setVisit(Visit visit) { this.visit = visit; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public Staff getLabTechnician() { return labTechnician; }
    public void setLabTechnician(Staff labTechnician) { this.labTechnician = labTechnician; }
    public Hospital getHospital() { return hospital; }
    public void setHospital(Hospital hospital) { this.hospital = hospital; }
    public String getTestType() { return testType; }
    public void setTestType(String testType) { this.testType = testType; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    public LocalDate getCollectionDate() { return collectionDate; }
    public void setCollectionDate(LocalDate collectionDate) { this.collectionDate = collectionDate; }
    public LocalDate getReportDate() { return reportDate; }
    public void setReportDate(LocalDate reportDate) { this.reportDate = reportDate; }
    public ReportStatus getStatus() { return status; }
    public void setStatus(ReportStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static LabReportBuilder builder() { return new LabReportBuilder(); }

    public static class LabReportBuilder {
        private LabReport r = new LabReport();
        public LabReportBuilder visit(Visit v) { r.visit = v; return this; }
        public LabReportBuilder patient(Patient p) { r.patient = p; return this; }
        public LabReportBuilder hospital(Hospital h) { r.hospital = h; return this; }
        public LabReportBuilder labTechnician(Staff lt) { r.labTechnician = lt; return this; }
        public LabReportBuilder testType(String t) { r.testType = t; return this; }
        public LabReportBuilder remarks(String rm) { r.remarks = rm; return this; }
        public LabReportBuilder fileName(String f) { r.fileName = f; return this; }
        public LabReportBuilder filePath(String p) { r.filePath = p; return this; }
        public LabReportBuilder fileSize(Long s) { r.fileSize = s; return this; }
        public LabReportBuilder fileType(String t) { r.fileType = t; return this; }
        public LabReportBuilder collectionDate(LocalDate d) { r.collectionDate = d; return this; }
        public LabReportBuilder reportDate(LocalDate d) { r.reportDate = d; return this; }
        public LabReportBuilder status(ReportStatus s) { r.status = s; return this; }
        public LabReport build() { return r; }
    }
}