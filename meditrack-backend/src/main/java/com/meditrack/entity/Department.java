package com.meditrack.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String headDoctor;
    private String floor;
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    private boolean active = true;

    public Department() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getHeadDoctor() { return headDoctor; }
    public void setHeadDoctor(String headDoctor) { this.headDoctor = headDoctor; }
    public String getFloor() { return floor; }
    public void setFloor(String floor) { this.floor = floor; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Hospital getHospital() { return hospital; }
    public void setHospital(Hospital hospital) { this.hospital = hospital; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public static DepartmentBuilder builder() { return new DepartmentBuilder(); }

    public static class DepartmentBuilder {
        private Department d = new Department();
        public DepartmentBuilder name(String n) { d.name = n; return this; }
        public DepartmentBuilder headDoctor(String h) { d.headDoctor = h; return this; }
        public DepartmentBuilder floor(String f) { d.floor = f; return this; }
        public DepartmentBuilder phone(String p) { d.phone = p; return this; }
        public DepartmentBuilder hospital(Hospital h) { d.hospital = h; return this; }
        public DepartmentBuilder active(boolean a) { d.active = a; return this; }
        public Department build() { return d; }
    }
}