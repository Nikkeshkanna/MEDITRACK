package com.meditrack.controller;

import com.meditrack.dto.*;
import com.meditrack.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired private AuthService authService;

    @PostMapping("/patient/login")
    public ResponseEntity<ApiResponse<AuthResponse>> patientLogin(
            @RequestBody AuthRequest request) {
        AuthResponse response = authService.patientLogin(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Login successful"));
    }

    @PostMapping("/patient/register")
    public ResponseEntity<ApiResponse<AuthResponse>> patientRegister(
            @Valid @RequestBody PatientRegisterRequest request) {
        AuthResponse response = authService.patientRegister(request);
        return ResponseEntity.ok(ApiResponse.success(response, response.getMessage()));
    }

    @PostMapping("/staff/login")
    public ResponseEntity<ApiResponse<AuthResponse>> staffLogin(
            @RequestBody AuthRequest request) {
        AuthResponse response = authService.staffLogin(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Login successful"));
    }

    @PostMapping("/admin/login")
    public ResponseEntity<ApiResponse<AuthResponse>> adminLogin(
            @RequestBody AuthRequest request) {
        AuthResponse response = authService.adminLogin(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Admin login successful"));
    }

    @GetMapping("/hospitals")
    public ResponseEntity<ApiResponse<?>> getHospitals() {
        return ResponseEntity.ok(ApiResponse.success(authService.getHospitalList()));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestBody java.util.Map<String, String> body) {
        return ResponseEntity.ok(ApiResponse.success("Reset link sent to your email/mobile"));
    }
}