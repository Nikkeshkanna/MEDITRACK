package com.meditrack.repository;

import com.meditrack.entity.LabReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabReportRepository extends JpaRepository<LabReport, Long> {

    List<LabReport> findByPatientIdOrderByCreatedAtDesc(Long patientId);

    List<LabReport> findByVisitId(Long visitId);

    List<LabReport> findByHospitalIdAndStatus(Long hospitalId, LabReport.ReportStatus status);

    List<LabReport> findByStatus(LabReport.ReportStatus status);

    long countByStatus(LabReport.ReportStatus status);
}