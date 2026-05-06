package com.meditrack.controller;

import com.meditrack.dto.ApiResponse;
import com.meditrack.service.EmergencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emergency")
public class EmergencyController {

    @Autowired private EmergencyService emergencyService;

    @GetMapping("/uhid/{uhid}")
    public ResponseEntity<ApiResponse<?>> getByUHID(@PathVariable String uhid) {
        return ResponseEntity.ok(ApiResponse.success(
                emergencyService.getEmergencyInfoByUHID(uhid)));
    }

    @GetMapping("/mobile/{mobile}")
    public ResponseEntity<ApiResponse<?>> getByMobile(@PathVariable String mobile) {
        return ResponseEntity.ok(ApiResponse.success(
                emergencyService.getEmergencyInfoByMobile(mobile)));
    }
}