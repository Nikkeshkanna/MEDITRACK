package com.meditrack.controller;

import com.meditrack.dto.ApiResponse;
import com.meditrack.service.SupportTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/support")
public class SupportTicketController {

    @Autowired private SupportTicketService supportTicketService;

    @PostMapping("/ticket")
    public ResponseEntity<ApiResponse<?>> create(
            @RequestBody Map<String, String> data,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.success(supportTicketService.createTicket(data, token)));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<?>> getMy(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.success(supportTicketService.getMyTickets(token)));
    }
}
