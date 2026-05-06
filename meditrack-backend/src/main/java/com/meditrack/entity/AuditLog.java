package com.meditrack.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@EntityListeners(AuditingEntityListener.class)
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String action;
    private String actor;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String hospital;
    private String ipAddress;

    @Column(columnDefinition = "TEXT")
    private String details;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime timestamp;

    public AuditLog() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getActor() { return actor; }
    public void setActor(String actor) { this.actor = actor; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public String getHospital() { return hospital; }
    public void setHospital(String hospital) { this.hospital = hospital; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public static AuditLogBuilder builder() { return new AuditLogBuilder(); }

    public static class AuditLogBuilder {
        private AuditLog l = new AuditLog();
        public AuditLogBuilder action(String a) { l.action = a; return this; }
        public AuditLogBuilder actor(String a) { l.actor = a; return this; }
        public AuditLogBuilder role(Role r) { l.role = r; return this; }
        public AuditLogBuilder hospital(String h) { l.hospital = h; return this; }
        public AuditLogBuilder details(String d) { l.details = d; return this; }
        public AuditLog build() { return l; }
    }
}