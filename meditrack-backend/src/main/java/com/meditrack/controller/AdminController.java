package com.meditrack.controller;

import com.meditrack.dto.*;
import com.meditrack.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired private AdminService adminService;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<?>> getDashboard() {
        return ResponseEntity.ok(ApiResponse.success(adminService.getDashboard()));
    }

    @GetMapping("/hospitals")
    public ResponseEntity<ApiResponse<?>> getHospitals() {
        return ResponseEntity.ok(ApiResponse.success(adminService.getHospitals()));
    }

    @PostMapping("/hospitals")
    public ResponseEntity<ApiResponse<?>> addHospital(@RequestBody HospitalRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                adminService.addHospital(request), "Hospital added successfully"));
    }

    @PutMapping("/hospitals/{id}")
    public ResponseEntity<ApiResponse<?>> updateHospital(
            @PathVariable Long id, @RequestBody HospitalRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                adminService.updateHospital(id, request), "Hospital updated"));
    }

    @PatchMapping("/hospitals/{id}/toggle")
    public ResponseEntity<ApiResponse<?>> toggleHospital(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(adminService.toggleHospital(id)));
    }

    @GetMapping("/staff")
    public ResponseEntity<ApiResponse<?>> getStaff(
            @RequestParam(required = false) Long hospitalId) {
        return ResponseEntity.ok(ApiResponse.success(adminService.getStaff(hospitalId)));
    }

    @PostMapping("/staff")
    public ResponseEntity<ApiResponse<?>> addStaff(@RequestBody StaffRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                adminService.addStaff(request), "Staff added successfully"));
    }

    @PutMapping("/staff/{id}")
    public ResponseEntity<ApiResponse<?>> updateStaff(
            @PathVariable Long id, @RequestBody StaffRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                adminService.updateStaff(id, request), "Staff updated"));
    }

    @PatchMapping("/staff/{id}/toggle")
    public ResponseEntity<ApiResponse<?>> toggleStaff(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(adminService.toggleStaff(id)));
    }

    @GetMapping("/departments")
    public ResponseEntity<ApiResponse<?>> getDepartments(@RequestParam Long hospitalId) {
        return ResponseEntity.ok(ApiResponse.success(adminService.getDepartments(hospitalId)));
    }

    @PostMapping("/departments")
    public ResponseEntity<ApiResponse<?>> addDepartment(@RequestBody Map<String, Object> data) {
        return ResponseEntity.ok(ApiResponse.success(
                adminService.addDepartment(data), "Department added"));
    }

    @GetMapping("/audit-logs")
    public ResponseEntity<ApiResponse<?>> getAuditLogs() {
        return ResponseEntity.ok(ApiResponse.success(adminService.getAuditLogs()));
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<?>> getStats() {
        return ResponseEntity.ok(ApiResponse.success(adminService.getDashboard()));
    }
}