package com.meditrack.repository;

import com.meditrack.entity.AuditLog;
import com.meditrack.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findTop100ByOrderByTimestampDesc();

    List<AuditLog> findByRole(Role role);

    List<AuditLog> findByActorContainingIgnoreCase(String actor);
}