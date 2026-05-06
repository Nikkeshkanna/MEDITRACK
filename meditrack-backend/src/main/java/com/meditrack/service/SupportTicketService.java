package com.meditrack.service;

import com.meditrack.entity.SupportTicket;
import com.meditrack.entity.User;
import com.meditrack.exception.ResourceNotFoundException;
import com.meditrack.repository.SupportTicketRepository;
import com.meditrack.repository.UserRepository;
import com.meditrack.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@SuppressWarnings("null")
public class SupportTicketService {

    @Autowired private SupportTicketRepository ticketRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private JwtUtil jwtUtil;

    @Transactional
    public Map<String, Object> createTicket(Map<String, String> data, String token) {
        Long userId = jwtUtil.extractUserId(token.substring(7));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        SupportTicket ticket = SupportTicket.builder()
                .user(user)
                .subject(data.get("subject"))
                .description(data.get("description"))
                .status(SupportTicket.TicketStatus.OPEN)
                .priority(SupportTicket.TicketPriority.valueOf(data.getOrDefault("priority", "MEDIUM").toUpperCase()))
                .build();

        ticketRepository.save(ticket);
        return Collections.singletonMap("message", "Ticket created successfully with ID: " + ticket.getId());
    }

    public List<SupportTicket> getMyTickets(String token) {
        Long userId = jwtUtil.extractUserId(token.substring(7));
        return ticketRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Transactional
    public Map<String, Object> resolveTicket(Long id, String response) {
        SupportTicket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "id", id));
        ticket.setAdminResponse(response);
        ticket.setStatus(SupportTicket.TicketStatus.RESOLVED);
        ticket.setClosedAt(LocalDateTime.now());
        ticketRepository.save(ticket);
        return Collections.singletonMap("message", "Ticket resolved");
    }
}
