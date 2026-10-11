package com.hms.config.audit;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.hms.entity.Role;
import com.hms.repository.RoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Runs once every time the application starts up.
 * Ensures the default roles exist in the database so that user
 * registration (which assigns an existing role, never creates one)
 * always has something to link to — even on a completely fresh database.
 *
 * Safe to run every restart: it checks existsByRoleName() first and
 * only inserts roles that are missing, so it won't create duplicates.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    private static final String[][] DEFAULT_ROLES = {
            { "ADMIN", "System administrator with full access" },
            { "DOCTOR", "Doctor role" },
            { "RECEPTIONIST", "Front-desk receptionist" },
            { "PATIENT", "Default self-registered patient role" }
    };

    @Override
    public void run(String... args) {
        for (String[] roleData : DEFAULT_ROLES) {
            String roleName = roleData[0];
            String description = roleData[1];

            if (!roleRepository.existsByRoleName(roleName)) {
                Role role = Role.builder()
                        .roleName(roleName)
                        .description(description)
                        .build();
                roleRepository.save(role);
                log.info("Seeded default role: {}", roleName);
            }
        }
    }
}