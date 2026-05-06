package com.meditrack.repository;

import com.meditrack.entity.ScanReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScanReportRepository extends JpaRepository<ScanReport, Long> {

    List<ScanReport> findByPatientIdOrderByCreatedAtDesc(Long patientId);

    List<ScanReport> findByVisitId(Long visitId);
}