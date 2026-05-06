package com.meditrack.repository;

import com.meditrack.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {

    List<Hospital> findByActiveTrue();

    @org.springframework.data.jpa.repository.Query("SELECT h FROM Hospital h WHERE (LOWER(h.name) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(h.district) LIKE LOWER(CONCAT('%', :q, '%'))) AND h.active = true")
    List<Hospital> searchHospitals(@org.springframework.data.repository.query.Param("q") String q);

    List<Hospital> findByDistrict(String district);

    boolean existsByRegistrationNumber(String registrationNumber);

    long countByActiveTrue();
}