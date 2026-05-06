package com.meditrack.repository;

import com.meditrack.entity.Role;
import com.meditrack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByMobile(String mobile);

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.email = :email OR u.mobile = :mobile OR u.username = :username")
    Optional<User> findByEmailOrMobileOrUsername(String email, String mobile, String username);

    boolean existsByEmail(String email);

    boolean existsByMobile(String mobile);

    long countByRole(Role role);
}