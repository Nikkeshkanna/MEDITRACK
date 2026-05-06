package com.meditrack.controller;

import com.meditrack.dto.ApiResponse;
import com.meditrack.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired private PatientService patientService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<?>> getProfile(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.success(patientService.getProfile(token)));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<?>> updateProfile(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, Object> data) {
        return ResponseEntity.ok(ApiResponse.success(
                patientService.updateProfile(token, data), "Profile updated"));
    }

    @PostMapping("/profile/photo")
    public ResponseEntity<ApiResponse<?>> uploadPhoto(
            @RequestHeader("Authorization") String token,
            @RequestParam("photo") MultipartFile file) throws Exception {
        return ResponseEntity.ok(ApiResponse.success(
                patientService.uploadProfilePhoto(token, file), "Photo updated"));
    }

    @GetMapping("/medical-history")
    public ResponseEntity<ApiResponse<?>> getMedicalHistory(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.success(patientService.getMedicalHistory(token)));
    }

    @GetMapping("/visits")
    public ResponseEntity<ApiResponse<?>> getVisits(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.success(patientService.getVisits(token)));
    }

    @GetMapping("/prescriptions")
    public ResponseEntity<ApiResponse<?>> getPrescriptions(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.success(patientService.getPrescriptions(token)));
    }

    @GetMapping("/lab-reports")
    public ResponseEntity<ApiResponse<?>> getLabReports(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.success(patientService.getLabReports(token)));
    }

    @GetMapping("/scan-reports")
    public ResponseEntity<ApiResponse<?>> getScanReports(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.success(patientService.getScanReports(token)));
    }

    @GetMapping("/emergency-info")
    public ResponseEntity<ApiResponse<?>> getEmergencyInfo(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.success(patientService.getEmergencyInfo(token)));
    }

    @GetMapping("/qr-code")
    public ResponseEntity<ApiResponse<?>> getQRCode(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.success(patientService.getQRCode(token)));
    }

    @GetMapping("/hospitals/search")
    public ResponseEntity<ApiResponse<?>> searchHospitals(@RequestParam(value = "q", defaultValue = "") String query) {
        return ResponseEntity.ok(ApiResponse.success(patientService.searchHospitals(query)));
    }
}