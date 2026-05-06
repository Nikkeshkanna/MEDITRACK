package com.meditrack.controller;

import com.meditrack.dto.ApiResponse;
import com.meditrack.service.LabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/reports")
public class LabController {

    @Autowired private LabService labService;

    @GetMapping("/lab/dashboard")
    public ResponseEntity<ApiResponse<?>> getDashboard(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.success(labService.getDashboard(token)));
    }

    @PostMapping("/lab")
    public ResponseEntity<ApiResponse<?>> uploadReport(
            @RequestParam("file") MultipartFile file,
            @RequestParam("patientId") Long patientId,
            @RequestParam(value = "visitId", required = false) Long visitId,
            @RequestParam("testType") String testType,
            @RequestParam(value = "remarks", required = false) String remarks,
            @RequestParam(value = "collectionDate", required = false) String collectionDate,
            @RequestHeader("Authorization") String token) throws Exception {
        return ResponseEntity.ok(ApiResponse.success(
                labService.uploadLabReport(file, patientId, visitId,
                        testType, remarks, collectionDate, token),
                "Report uploaded successfully"));
    }

    @GetMapping("/lab/pending")
    public ResponseEntity<ApiResponse<?>> getPendingTests(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.success(labService.getPendingTests(token)));
    }

    @PatchMapping("/lab/{id}/status")
    public ResponseEntity<ApiResponse<?>> updateStatus(
            @PathVariable Long id,
            @RequestBody java.util.Map<String, String> body,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.success(
                labService.updateTestStatus(id, body.get("status")),
                "Status updated"));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadReport(@PathVariable Long id) throws Exception {
        byte[] data = labService.downloadReport(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=report-" + id + ".pdf")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(data);
    }
}