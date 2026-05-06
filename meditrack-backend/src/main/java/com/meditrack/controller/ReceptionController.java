package com.meditrack.controller;

import com.meditrack.dto.ApiResponse;
import com.meditrack.service.ReceptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/reception")
public class ReceptionController {

    @Autowired private ReceptionService receptionService;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<?>> getDashboard(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.success(receptionService.getDashboard(token)));
    }

    @GetMapping("/search-patient")
    public ResponseEntity<ApiResponse<?>> searchPatient(
            @RequestParam String q,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.success(receptionService.searchPatient(q, token)));
    }

    @PostMapping("/register-patient")
    public ResponseEntity<ApiResponse<?>> registerPatient(
            @RequestBody com.meditrack.dto.PatientRegisterRequest data,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.success(
                receptionService.registerPatient(data, token),
                "Patient registered and checked in successfully"));
    }

    @PostMapping("/checkin")
    public ResponseEntity<ApiResponse<?>> checkIn(
            @RequestBody Map<String, Object> data,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.success(
                receptionService.checkInPatient(data, token),
                "Patient checked in successfully"));
    }
}