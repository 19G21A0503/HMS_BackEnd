package com.hms.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hms.entity.OtpVerification;
import com.hms.entity.User;
import com.hms.enums.OtpPurpose;

@Repository
public interface OtpVerificationRepository extends JpaRepository<OtpVerification, UUID> {

    Optional<OtpVerification>
    findTopByUserAndPurposeAndVerifiedFalseOrderByCreatedAtDesc(
            User user,
            OtpPurpose purpose
    );

}