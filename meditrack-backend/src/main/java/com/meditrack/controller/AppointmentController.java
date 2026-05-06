package com.meditrack.controller;

import com.meditrack.dto.ApiResponse;
import com.meditrack.dto.AppointmentRequest;
import com.meditrack.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired private AppointmentService appointmentService;

    @PostMapping("/book")
    public ResponseEntity<ApiResponse<?>> book(
            @RequestBody AppointmentRequest request,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.success(appointmentService.bookAppointment(request, token)));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<?>> getMy(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.success(appointmentService.getPatientAppointments(token)));
    }

    @GetMapping("/doctor-today")
    public ResponseEntity<ApiResponse<?>> getDoctorToday(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.success(appointmentService.getDoctorAppointments(token)));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<?>> updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.success(appointmentService.updateStatus(id, status, token)));
    }
}
