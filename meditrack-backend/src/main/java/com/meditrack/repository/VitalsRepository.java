package com.meditrack.repository;

import com.meditrack.entity.Vitals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VitalsRepository extends JpaRepository<Vitals, Long> {

    Optional<Vitals> findByVisitId(Long visitId);
}