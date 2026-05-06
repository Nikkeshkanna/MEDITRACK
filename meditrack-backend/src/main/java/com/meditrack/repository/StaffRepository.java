package com.meditrack.repository;

import com.meditrack.entity.Role;
import com.meditrack.entity.Staff;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    @EntityGraph(attributePaths = {"hospital", "user"})
    Optional<Staff> findByUserId(Long userId);

    @EntityGraph(attributePaths = {"hospital", "user"})
    List<Staff> findByHospitalId(Long hospitalId);

    @EntityGraph(attributePaths = {"hospital", "user"})
    List<Staff> findAll();

    List<Staff> findByHospitalIdAndRole(Long hospitalId, Role role);

    List<Staff> findByRole(Role role);

    @Query("SELECT s FROM Staff s WHERE " +
           "LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(s.email) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Staff> searchStaff(@Param("query") String query);

    boolean existsByEmail(String email);

    long countByRole(Role role);
}