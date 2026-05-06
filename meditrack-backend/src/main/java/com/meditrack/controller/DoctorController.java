package com.meditrack.controller;

import com.meditrack.dto.*;
import com.meditrack.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired private DoctorService doctorService;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<?>> getDashboard(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.success(doctorService.getDashboard(token)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<?>> searchPatient(@RequestParam("q") String query) {
        return ResponseEntity.ok(ApiResponse.success(doctorService.searchPatient(query)));
    }

    @GetMapping("/patient/{uhid}")
    public ResponseEntity<ApiResponse<?>> getPatientByUHID(@PathVariable String uhid) {
        return ResponseEntity.ok(ApiResponse.success(doctorService.getPatientByUHID(uhid)));
    }

    @GetMapping("/patient/{uhid}/history")
    public ResponseEntity<ApiResponse<?>> getPatientHistory(@PathVariable String uhid) {
        return ResponseEntity.ok(ApiResponse.success(doctorService.getPatientHistory(uhid)));
    }

    @GetMapping("/today-patients")
    public ResponseEntity<ApiResponse<?>> getTodayPatients(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.success(doctorService.getTodayPatients(token)));
    }

    @PostMapping("/visit/{visitId}/diagnosis")
    public ResponseEntity<ApiResponse<?>> addDiagnosis(
            @PathVariable Long visitId,
            @RequestBody DiagnosisRequest request,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.success(
                doctorService.addDiagnosis(visitId, request, token),
                "Diagnosis saved successfully"));
    }

    @PostMapping("/visit/{visitId}/prescription")
    public ResponseEntity<ApiResponse<?>> addPrescription(
            @PathVariable Long visitId,
            @RequestBody PrescriptionRequest request,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.success(
                doctorService.addPrescription(visitId, request, token),
                "Prescription saved successfully"));
    }

    @PostMapping("/visit/{visitId}/notes")
    public ResponseEntity<ApiResponse<?>> addNotes(
            @PathVariable Long visitId,
            @RequestBody java.util.Map<String, String> body,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.success("Notes saved"));
    }
}