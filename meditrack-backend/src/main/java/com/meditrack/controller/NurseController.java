package com.meditrack.controller;

import com.meditrack.dto.*;
import com.meditrack.service.NurseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/nurse")
public class NurseController {

    @Autowired private NurseService nurseService;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<?>> getDashboard(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.success(nurseService.getDashboard(token)));
    }

    @GetMapping("/queue")
    public ResponseEntity<ApiResponse<?>> getQueue(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.success(nurseService.getPatientQueue(token)));
    }

    @PostMapping("/visit/{visitId}/vitals")
    public ResponseEntity<ApiResponse<?>> addVitals(
            @PathVariable Long visitId,
            @RequestBody VitalsRequest request,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.success(
                nurseService.addVitals(visitId, request, token),
                "Vitals recorded successfully"));
    }

    @PostMapping("/visit/{visitId}/forward")
    public ResponseEntity<ApiResponse<?>> sendToDoctor(
            @PathVariable Long visitId,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.success(
                nurseService.sendToDoctor(visitId, token),
                "Patient forwarded to doctor"));
    }
}