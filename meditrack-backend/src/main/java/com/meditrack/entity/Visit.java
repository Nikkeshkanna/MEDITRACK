package com.meditrack.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "visits")
@EntityListeners(AuditingEntityListener.class)
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tokenNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Staff doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nurse_id")
    private Staff nurse;

    private String department;

    @Enumerated(EnumType.STRING)
    private VisitType visitType;

    @Enumerated(EnumType.STRING)
    private VisitStatus status;

    private LocalDate visitDate;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;

    @Column(columnDefinition = "TEXT")
    private String chiefComplaint;

    @OneToOne(mappedBy = "visit", cascade = CascadeType.ALL)
    private Diagnosis diagnosis;

    @OneToMany(mappedBy = "visit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Prescription> prescriptions;

    @OneToOne(mappedBy = "visit", cascade = CascadeType.ALL)
    private Vitals vitals;

    @OneToMany(mappedBy = "visit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LabReport> labReports;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public enum VisitType {
        OPD, EMERGENCY, FOLLOW_UP, CONSULTATION
    }

    public enum VisitStatus {
        WAITING, WITH_NURSE, WITH_DOCTOR, COMPLETED, CANCELLED
    }

    public Visit() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTokenNumber() { return tokenNumber; }
    public void setTokenNumber(String tokenNumber) { this.tokenNumber = tokenNumber; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public Hospital getHospital() { return hospital; }
    public void setHospital(Hospital hospital) { this.hospital = hospital; }
    public Staff getDoctor() { return doctor; }
    public void setDoctor(Staff doctor) { this.doctor = doctor; }
    public Staff getNurse() { return nurse; }
    public void setNurse(Staff nurse) { this.nurse = nurse; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public VisitType getVisitType() { return visitType; }
    public void setVisitType(VisitType visitType) { this.visitType = visitType; }
    public VisitStatus getStatus() { return status; }
    public void setStatus(VisitStatus status) { this.status = status; }
    public LocalDate getVisitDate() { return visitDate; }
    public void setVisitDate(LocalDate visitDate) { this.visitDate = visitDate; }
    public LocalDateTime getCheckInTime() { return checkInTime; }
    public void setCheckInTime(LocalDateTime checkInTime) { this.checkInTime = checkInTime; }
    public LocalDateTime getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(LocalDateTime checkOutTime) { this.checkOutTime = checkOutTime; }
    public String getChiefComplaint() { return chiefComplaint; }
    public void setChiefComplaint(String chiefComplaint) { this.chiefComplaint = chiefComplaint; }
    public Diagnosis getDiagnosis() { return diagnosis; }
    public void setDiagnosis(Diagnosis diagnosis) { this.diagnosis = diagnosis; }
    public List<Prescription> getPrescriptions() { return prescriptions; }
    public void setPrescriptions(List<Prescription> prescriptions) { this.prescriptions = prescriptions; }
    public Vitals getVitals() { return vitals; }
    public void setVitals(Vitals vitals) { this.vitals = vitals; }
    public List<LabReport> getLabReports() { return labReports; }
    public void setLabReports(List<LabReport> labReports) { this.labReports = labReports; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static VisitBuilder builder() { return new VisitBuilder(); }

    public static class VisitBuilder {
        private Visit v = new Visit();
        public VisitBuilder tokenNumber(String t) { v.tokenNumber = t; return this; }
        public VisitBuilder patient(Patient p) { v.patient = p; return this; }
        public VisitBuilder hospital(Hospital h) { v.hospital = h; return this; }
        public VisitBuilder doctor(Staff d) { v.doctor = d; return this; }
        public VisitBuilder nurse(Staff n) { v.nurse = n; return this; }
        public VisitBuilder department(String d) { v.department = d; return this; }
        public VisitBuilder visitType(VisitType t) { v.visitType = t; return this; }
        public VisitBuilder status(VisitStatus s) { v.status = s; return this; }
        public VisitBuilder visitDate(LocalDate d) { v.visitDate = d; return this; }
        public VisitBuilder checkInTime(LocalDateTime t) { v.checkInTime = t; return this; }
        public VisitBuilder chiefComplaint(String c) { v.chiefComplaint = c; return this; }
        public Visit build() { return v; }
    }
}