package com.meditrack.config;

import com.meditrack.entity.*;
import com.meditrack.entity.User;
import com.meditrack.entity.Role;
import com.meditrack.repository.*;
import com.meditrack.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    @Autowired private UserRepository userRepository;
    @Autowired private HospitalRepository hospitalRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Value("${meditrack.admin.email}")
    private String adminEmail;

    @Value("${meditrack.admin.password}")
    private String adminPassword;

    @Value("${meditrack.admin.name}")
    private String adminName;

    @Autowired private StaffRepository staffRepository;

    @Override
    public void run(String... args) {
        seedSampleHospital();
        seedAdminAndStaff();
    }

    private void seedAdminAndStaff() {
        User admin = userRepository.findByEmail(adminEmail).orElse(null);
        if (admin == null) {
            boolean adminExistsByRole = userRepository.countByRole(Role.ADMIN) > 0;
            if (!adminExistsByRole) {
                admin = User.builder()
                        .name(adminName)
                        .email(adminEmail)
                        .username("admin")
                        .password(passwordEncoder.encode(adminPassword))
                        .role(Role.ADMIN)
                        .active(true)
                        .build();
                admin = userRepository.save(admin);
                log.info("Admin account created: {}", adminEmail);
            } else {
                admin = userRepository.findAll().stream()
                        .filter(u -> u.getRole() == Role.ADMIN)
                        .findFirst().orElse(null);
            }
        }

        if (admin != null) {
            if (staffRepository.findByUserId(admin.getId()).isEmpty()) {
                Hospital hospital = hospitalRepository.findAll().stream().findFirst().orElse(null);
                if (hospital != null) {
                    Staff adminStaff = Staff.builder()
                            .user(admin)
                            .name(admin.getName())
                            .email(admin.getEmail())
                            .role(Role.ADMIN)
                            .hospital(hospital)
                            .active(true)
                            .department("Administration")
                            .build();
                    staffRepository.save(adminStaff);
                    log.info("Admin staff record created for hospital: {}", hospital.getName());
                }
            }
        }

        // Seed Doctor
        if (userRepository.findByEmail("doctor@meditrack.com").isEmpty()) {
            User doctor = User.builder()
                    .name("Dr. Ramesh Kumar")
                    .email("doctor@meditrack.com")
                    .username("doctor")
                    .password(passwordEncoder.encode("MediTrack@2026"))
                    .role(Role.DOCTOR)
                    .active(true)
                    .build();
            userRepository.save(doctor);
            Hospital hospital = hospitalRepository.findAll().stream().findFirst().orElse(null);
            if (hospital != null) {
                staffRepository.save(Staff.builder()
                        .user(doctor).name("Dr. Ramesh Kumar").email("doctor@meditrack.com").role(Role.DOCTOR)
                        .hospital(hospital).department("General Medicine").active(true).build());
            }
        }

        // Seed Nurse
        if (userRepository.findByEmail("nurse@meditrack.com").isEmpty()) {
            User nurse = User.builder()
                    .name("Nurse Anitha")
                    .email("nurse@meditrack.com")
                    .username("nurse")
                    .password(passwordEncoder.encode("MediTrack@2026"))
                    .role(Role.NURSE)
                    .active(true)
                    .build();
            userRepository.save(nurse);
            Hospital hospital = hospitalRepository.findAll().stream().findFirst().orElse(null);
            if (hospital != null) {
                staffRepository.save(Staff.builder()
                        .user(nurse).name("Nurse Anitha").email("nurse@meditrack.com").role(Role.NURSE)
                        .hospital(hospital).department("General Medicine").active(true).build());
            }
        }

        // Seed Receptionist
        if (userRepository.findByEmail("receptionist@meditrack.com").isEmpty()) {
            User rec = User.builder()
                    .name("Front Desk")
                    .email("receptionist@meditrack.com")
                    .username("receptionist")
                    .password(passwordEncoder.encode("MediTrack@2026"))
                    .role(Role.RECEPTIONIST)
                    .active(true)
                    .build();
            userRepository.save(rec);
            Hospital hospital = hospitalRepository.findAll().stream().findFirst().orElse(null);
            if (hospital != null) {
                staffRepository.save(Staff.builder()
                        .user(rec).name("Front Desk").email("receptionist@meditrack.com").role(Role.RECEPTIONIST)
                        .hospital(hospital).department("Reception").active(true).build());
            }
        }
    }

    private void seedSampleHospital() {
        if (hospitalRepository.count() == 0) {
            Hospital hospital = Hospital.builder()
                    .name("City General Hospital")
                    .district("Salem")
                    .address("123 Main Road, Salem, Tamil Nadu - 636001")
                    .phone("04272-123456")
                    .email("admin@citygeneral.com")
                    .registrationNumber("TN-MED-2024-001")
                    .type(Hospital.HospitalType.GOVERNMENT)
                    .totalBeds(300)
                    .active(true)
                    .build();
            hospitalRepository.save(hospital);
            log.info("Sample hospital seeded: City General Hospital");
        }
    }
}