package com.meditrack.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "diagnoses")
@EntityListeners(AuditingEntityListener.class)
public class Diagnosis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visit_id", nullable = false)
    private Visit visit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Staff doctor;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String primaryDiagnosis;

    @Column(columnDefinition = "TEXT")
    private String secondaryDiagnosis;

    @Column(columnDefinition = "TEXT")
    private String symptoms;

    @Column(columnDefinition = "TEXT")
    private String clinicalNotes;

    private boolean followUpRequired;
    private LocalDate followUpDate;

    @Column(columnDefinition = "TEXT")
    private String followUpNotes;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public Diagnosis() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Visit getVisit() { return visit; }
    public void setVisit(Visit visit) { this.visit = visit; }
    public Staff getDoctor() { return doctor; }
    public void setDoctor(Staff doctor) { this.doctor = doctor; }
    public String getPrimaryDiagnosis() { return primaryDiagnosis; }
    public void setPrimaryDiagnosis(String primaryDiagnosis) { this.primaryDiagnosis = primaryDiagnosis; }
    public String getSecondaryDiagnosis() { return secondaryDiagnosis; }
    public void setSecondaryDiagnosis(String secondaryDiagnosis) { this.secondaryDiagnosis = secondaryDiagnosis; }
    public String getSymptoms() { return symptoms; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }
    public String getClinicalNotes() { return clinicalNotes; }
    public void setClinicalNotes(String clinicalNotes) { this.clinicalNotes = clinicalNotes; }
    public boolean isFollowUpRequired() { return followUpRequired; }
    public void setFollowUpRequired(boolean followUpRequired) { this.followUpRequired = followUpRequired; }
    public LocalDate getFollowUpDate() { return followUpDate; }
    public void setFollowUpDate(LocalDate followUpDate) { this.followUpDate = followUpDate; }
    public String getFollowUpNotes() { return followUpNotes; }
    public void setFollowUpNotes(String followUpNotes) { this.followUpNotes = followUpNotes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static DiagnosisBuilder builder() { return new DiagnosisBuilder(); }

    public static class DiagnosisBuilder {
        private Diagnosis d = new Diagnosis();
        public DiagnosisBuilder visit(Visit v) { d.visit = v; return this; }
        public DiagnosisBuilder doctor(Staff dr) { d.doctor = dr; return this; }
        public DiagnosisBuilder primaryDiagnosis(String p) { d.primaryDiagnosis = p; return this; }
        public DiagnosisBuilder secondaryDiagnosis(String s) { d.secondaryDiagnosis = s; return this; }
        public DiagnosisBuilder symptoms(String s) { d.symptoms = s; return this; }
        public DiagnosisBuilder clinicalNotes(String n) { d.clinicalNotes = n; return this; }
        public DiagnosisBuilder followUpRequired(boolean f) { d.followUpRequired = f; return this; }
        public DiagnosisBuilder followUpDate(LocalDate fd) { d.followUpDate = fd; return this; }
        public DiagnosisBuilder followUpNotes(String n) { d.followUpNotes = n; return this; }
        public Diagnosis build() { return d; }
    }
}