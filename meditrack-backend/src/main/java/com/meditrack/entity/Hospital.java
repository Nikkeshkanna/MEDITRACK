package com.meditrack.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "hospitals")
@EntityListeners(AuditingEntityListener.class)
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private String address;

    private String phone;
    private String email;
    private String registrationNumber;

    @Enumerated(EnumType.STRING)
    private HospitalType type;

    private Integer totalBeds;

    @Column(nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Department> departments;

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Staff> staffMembers;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public enum HospitalType {
        GOVERNMENT, PRIVATE, TRUST, CLINIC
    }

    public Hospital() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
    public HospitalType getType() { return type; }
    public void setType(HospitalType type) { this.type = type; }
    public Integer getTotalBeds() { return totalBeds; }
    public void setTotalBeds(Integer totalBeds) { this.totalBeds = totalBeds; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public List<Department> getDepartments() { return departments; }
    public void setDepartments(List<Department> departments) { this.departments = departments; }
    public List<Staff> getStaffMembers() { return staffMembers; }
    public void setStaffMembers(List<Staff> staffMembers) { this.staffMembers = staffMembers; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static HospitalBuilder builder() { return new HospitalBuilder(); }

    public static class HospitalBuilder {
        private Hospital h = new Hospital();
        public HospitalBuilder name(String n) { h.name = n; return this; }
        public HospitalBuilder district(String d) { h.district = d; return this; }
        public HospitalBuilder address(String a) { h.address = a; return this; }
        public HospitalBuilder phone(String p) { h.phone = p; return this; }
        public HospitalBuilder email(String e) { h.email = e; return this; }
        public HospitalBuilder registrationNumber(String r) { h.registrationNumber = r; return this; }
        public HospitalBuilder type(HospitalType t) { h.type = t; return this; }
        public HospitalBuilder totalBeds(Integer b) { h.totalBeds = b; return this; }
        public HospitalBuilder active(boolean a) { h.active = a; return this; }
        public Hospital build() { return h; }
    }
}