package com.meditrack.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "prescriptions")
@EntityListeners(AuditingEntityListener.class)
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visit_id", nullable = false)
    private Visit visit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Staff doctor;

    @Column(nullable = false)
    private String medicineName;

    private String dosage;
    private String frequency;
    private String duration;

    @Column(columnDefinition = "TEXT")
    private String instructions;

    @Column(columnDefinition = "TEXT")
    private String notes;

    private LocalDate prescriptionDate;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public Prescription() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Visit getVisit() { return visit; }
    public void setVisit(Visit visit) { this.visit = visit; }
    public Staff getDoctor() { return doctor; }
    public void setDoctor(Staff doctor) { this.doctor = doctor; }
    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }
    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }
    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDate getPrescriptionDate() { return prescriptionDate; }
    public void setPrescriptionDate(LocalDate prescriptionDate) { this.prescriptionDate = prescriptionDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static PrescriptionBuilder builder() { return new PrescriptionBuilder(); }

    public static class PrescriptionBuilder {
        private Prescription p = new Prescription();
        public PrescriptionBuilder visit(Visit v) { p.visit = v; return this; }
        public PrescriptionBuilder doctor(Staff d) { p.doctor = d; return this; }
        public PrescriptionBuilder medicineName(String m) { p.medicineName = m; return this; }
        public PrescriptionBuilder dosage(String d) { p.dosage = d; return this; }
        public PrescriptionBuilder frequency(String f) { p.frequency = f; return this; }
        public PrescriptionBuilder duration(String d) { p.duration = d; return this; }
        public PrescriptionBuilder instructions(String i) { p.instructions = i; return this; }
        public PrescriptionBuilder notes(String n) { p.notes = n; return this; }
        public PrescriptionBuilder prescriptionDate(LocalDate d) { p.prescriptionDate = d; return this; }
        public Prescription build() { return p; }
    }
}