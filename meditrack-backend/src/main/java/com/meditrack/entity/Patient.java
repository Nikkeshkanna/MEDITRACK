package com.meditrack.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "patients")
@EntityListeners(AuditingEntityListener.class)
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String uhid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String mobile;

    private String email;
    private LocalDate dob;
    private String gender;
    private String bloodGroup;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(columnDefinition = "TEXT")
    private String allergies;

    @Column(columnDefinition = "TEXT")
    private String chronicDiseases;

    @Column(columnDefinition = "TEXT")
    private String currentMedications;

    private String emergencyContactName;
    private String emergencyContactMobile;
    private String emergencyContactRelation;
    private String photoUrl;

    @Column(columnDefinition = "TEXT")
    private String qrCodeData;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Visit> visits;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public Patient() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUhid() { return uhid; }
    public void setUhid(String uhid) { this.uhid = uhid; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getAllergies() { return allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }
    public String getChronicDiseases() { return chronicDiseases; }
    public void setChronicDiseases(String chronicDiseases) { this.chronicDiseases = chronicDiseases; }
    public String getCurrentMedications() { return currentMedications; }
    public void setCurrentMedications(String currentMedications) { this.currentMedications = currentMedications; }
    public String getEmergencyContactName() { return emergencyContactName; }
    public void setEmergencyContactName(String emergencyContactName) { this.emergencyContactName = emergencyContactName; }
    public String getEmergencyContactMobile() { return emergencyContactMobile; }
    public void setEmergencyContactMobile(String emergencyContactMobile) { this.emergencyContactMobile = emergencyContactMobile; }
    public String getEmergencyContactRelation() { return emergencyContactRelation; }
    public void setEmergencyContactRelation(String emergencyContactRelation) { this.emergencyContactRelation = emergencyContactRelation; }
    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public String getQrCodeData() { return qrCodeData; }
    public void setQrCodeData(String qrCodeData) { this.qrCodeData = qrCodeData; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public List<Visit> getVisits() { return visits; }
    public void setVisits(List<Visit> visits) { this.visits = visits; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static PatientBuilder builder() { return new PatientBuilder(); }

    public static class PatientBuilder {
        private Patient p = new Patient();
        public PatientBuilder uhid(String u) { p.uhid = u; return this; }
        public PatientBuilder name(String n) { p.name = n; return this; }
        public PatientBuilder mobile(String m) { p.mobile = m; return this; }
        public PatientBuilder email(String e) { p.email = e; return this; }
        public PatientBuilder dob(LocalDate d) { p.dob = d; return this; }
        public PatientBuilder gender(String g) { p.gender = g; return this; }
        public PatientBuilder bloodGroup(String b) { p.bloodGroup = b; return this; }
        public PatientBuilder address(String a) { p.address = a; return this; }
        public PatientBuilder allergies(String a) { p.allergies = a; return this; }
        public PatientBuilder chronicDiseases(String c) { p.chronicDiseases = c; return this; }
        public PatientBuilder currentMedications(String m) { p.currentMedications = m; return this; }
        public PatientBuilder emergencyContactName(String n) { p.emergencyContactName = n; return this; }
        public PatientBuilder emergencyContactMobile(String m) { p.emergencyContactMobile = m; return this; }
        public PatientBuilder emergencyContactRelation(String r) { p.emergencyContactRelation = r; return this; }
        public PatientBuilder user(User u) { p.user = u; return this; }
        public Patient build() { return p; }
    }
}