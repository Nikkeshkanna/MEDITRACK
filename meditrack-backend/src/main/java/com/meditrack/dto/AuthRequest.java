package com.meditrack.dto;

public class AuthRequest {
    private String identifier;
    private String password;
    private Long hospitalId;

    public AuthRequest() {}

    public AuthRequest(String identifier, String password, Long hospitalId) {
        this.identifier = identifier;
        this.password = password;
        this.hospitalId = hospitalId;
    }

    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Long getHospitalId() { return hospitalId; }
    public void setHospitalId(Long hospitalId) { this.hospitalId = hospitalId; }
}