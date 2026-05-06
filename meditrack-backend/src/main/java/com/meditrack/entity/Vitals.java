package com.meditrack.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "vitals")
@EntityListeners(AuditingEntityListener.class)
public class Vitals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visit_id", nullable = false)
    private Visit visit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nurse_id")
    private Staff nurse;

    private Integer bloodPressureSystolic;
    private Integer bloodPressureDiastolic;
    private Integer pulseRate;
    private Double temperature;
    private Double weight;
    private Double height;
    private Double bmi;
    private Integer oxygenSaturation;
    private Double bloodSugar;

    @Column(columnDefinition = "TEXT")
    private String symptoms;

    @Column(columnDefinition = "TEXT")
    private String nursingNotes;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime recordedAt;

    public Vitals() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Visit getVisit() { return visit; }
    public void setVisit(Visit visit) { this.visit = visit; }
    public Staff getNurse() { return nurse; }
    public void setNurse(Staff nurse) { this.nurse = nurse; }
    public Integer getBloodPressureSystolic() { return bloodPressureSystolic; }
    public void setBloodPressureSystolic(Integer bloodPressureSystolic) { this.bloodPressureSystolic = bloodPressureSystolic; }
    public Integer getBloodPressureDiastolic() { return bloodPressureDiastolic; }
    public void setBloodPressureDiastolic(Integer bloodPressureDiastolic) { this.bloodPressureDiastolic = bloodPressureDiastolic; }
    public Integer getPulseRate() { return pulseRate; }
    public void setPulseRate(Integer pulseRate) { this.pulseRate = pulseRate; }
    public Double getTemperature() { return temperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
    public Double getHeight() { return height; }
    public void setHeight(Double height) { this.height = height; }
    public Double getBmi() { return bmi; }
    public void setBmi(Double bmi) { this.bmi = bmi; }
    public Integer getOxygenSaturation() { return oxygenSaturation; }
    public void setOxygenSaturation(Integer oxygenSaturation) { this.oxygenSaturation = oxygenSaturation; }
    public Double getBloodSugar() { return bloodSugar; }
    public void setBloodSugar(Double bloodSugar) { this.bloodSugar = bloodSugar; }
    public String getSymptoms() { return symptoms; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }
    public String getNursingNotes() { return nursingNotes; }
    public void setNursingNotes(String nursingNotes) { this.nursingNotes = nursingNotes; }
    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }

    public static VitalsBuilder builder() { return new VitalsBuilder(); }

    public static class VitalsBuilder {
        private Vitals v = new Vitals();
        public VitalsBuilder visit(Visit vt) { v.visit = vt; return this; }
        public VitalsBuilder nurse(Staff n) { v.nurse = n; return this; }
        public VitalsBuilder bloodPressureSystolic(Integer s) { v.bloodPressureSystolic = s; return this; }
        public VitalsBuilder bloodPressureDiastolic(Integer d) { v.bloodPressureDiastolic = d; return this; }
        public VitalsBuilder pulseRate(Integer p) { v.pulseRate = p; return this; }
        public VitalsBuilder temperature(Double t) { v.temperature = t; return this; }
        public VitalsBuilder weight(Double w) { v.weight = w; return this; }
        public VitalsBuilder height(Double h) { v.height = h; return this; }
        public VitalsBuilder bmi(Double b) { v.bmi = b; return this; }
        public VitalsBuilder oxygenSaturation(Integer o) { v.oxygenSaturation = o; return this; }
        public VitalsBuilder bloodSugar(Double s) { v.bloodSugar = s; return this; }
        public VitalsBuilder symptoms(String s) { v.symptoms = s; return this; }
        public VitalsBuilder nursingNotes(String n) { v.nursingNotes = n; return this; }
        public VitalsBuilder recordedAt(LocalDateTime r) { v.recordedAt = r; return this; }
        public Vitals build() { return v; }
    }
}