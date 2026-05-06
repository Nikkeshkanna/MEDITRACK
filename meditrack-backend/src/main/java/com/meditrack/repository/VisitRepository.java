package com.meditrack.repository;

import com.meditrack.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {

    List<Visit> findByPatientIdOrderByVisitDateDesc(Long patientId);

    List<Visit> findByHospitalIdAndVisitDate(Long hospitalId, LocalDate date);

    List<Visit> findByDoctorIdAndVisitDate(Long doctorId, LocalDate date);

    List<Visit> findByNurseIdAndVisitDate(Long nurseId, LocalDate date);

    List<Visit> findByHospitalIdAndStatusNotOrderByCheckInTimeAsc(
            Long hospitalId, Visit.VisitStatus status);

    List<Visit> findByHospitalIdAndDepartmentAndVisitDate(
            Long hospitalId, String department, LocalDate date);

    Optional<Visit> findByTokenNumber(String tokenNumber);

    @Query("SELECT COUNT(v) FROM Visit v WHERE v.hospital.id = :hospitalId AND v.visitDate = :date")
    long countTodayVisits(@Param("hospitalId") Long hospitalId, @Param("date") LocalDate date);

    @Query("SELECT v FROM Visit v WHERE v.patient.id = :patientId " +
           "AND v.status != 'COMPLETED' AND v.status != 'CANCELLED' ORDER BY v.checkInTime DESC")
    Optional<Visit> findActiveVisitByPatient(@Param("patientId") Long patientId);

    long countByVisitDate(LocalDate date);
}