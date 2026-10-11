package com.hms.serviceImpl;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.hms.constant.ApplicationConstants;
import com.hms.dto.SendOtpRequestDTO;
import com.hms.dto.VerifyOtpRequestDTO;
import com.hms.entity.OtpVerification;
import com.hms.entity.User;
import com.hms.enums.OtpPurpose;
import com.hms.exception.InvalidOtpException;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.OtpVerificationRepository;
import com.hms.repository.UserRepository;
import com.hms.service.OtpService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class OtpServiceImpl implements OtpService {

	private static final int OTP_LENGTH = 6;
	private static final int OTP_VALID_MINUTES = 5;
	private static final int MAX_ATTEMPTS = 5;

	private static final OtpPurpose DEFAULT_PURPOSE = OtpPurpose.LOGIN;

	private final OtpVerificationRepository otpVerificationRepository;
	private final UserRepository userRepository;
	private final JavaMailSender mailSender;

	private final SecureRandom secureRandom = new SecureRandom();

	@Override
	public void sendOtp(SendOtpRequestDTO request) {

		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException(ApplicationConstants.USER_NOT_FOUND));

		String otpCode = generateOtp();
		OtpVerification otp = OtpVerification.builder()
				.user(user)
				.otpCode(otpCode)
				.purpose(DEFAULT_PURPOSE)
				.expiryTime(LocalDateTime.now().plusMinutes(OTP_VALID_MINUTES))
				.verified(false)
				.attempts(0)
				.build();

		otpVerificationRepository.save(otp);
		sendOtpEmail(user.getEmail(), otpCode);
		log.info("OTP sent successfully to user: {}", user.getEmail());
	}

	// Send OTP to Email Logic
	private void sendOtpEmail(String email, String otpCode) {

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("Hospital Management System - OTP Verification");
		message.setText("Dear User,\n\n" + "Your OTP for Hospital Management System is: " + otpCode + "\n\n"
				+ "This OTP is valid for " + OTP_VALID_MINUTES + " minutes.\n\n"
				+ "Please do not share this OTP with anyone.\n\n" + "Regards,\n" + "Hospital Management System");

		mailSender.send(message);
		log.info("OTP email sent to {}", email);
	}

	@Override
	public void verifyOtp(VerifyOtpRequestDTO request) {

		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException(ApplicationConstants.USER_NOT_FOUND));

		OtpVerification otp = otpVerificationRepository
				.findTopByUserAndPurposeAndVerifiedFalseOrderByCreatedAtDesc(user, DEFAULT_PURPOSE)
				.orElseThrow(() -> new InvalidOtpException(ApplicationConstants.INVALID_OTP));

		if (otp.getExpiryTime().isBefore(LocalDateTime.now())) {
			throw new InvalidOtpException(ApplicationConstants.OTP_EXPIRED);
		}

		if (otp.getAttempts() != null && otp.getAttempts() >= MAX_ATTEMPTS) {
			throw new InvalidOtpException(ApplicationConstants.OTP_EXPIRED);
		}

		if (!otp.getOtpCode().equals(request.getOtp())) {
			otp.setAttempts(otp.getAttempts() == null ? 1 : otp.getAttempts() + 1);
			otpVerificationRepository.save(otp);
			throw new InvalidOtpException(ApplicationConstants.INVALID_OTP);
		}

		otp.setVerified(true);
		otpVerificationRepository.save(otp);
		log.info("OTP verified successfully for user: {}", user.getEmail());
	}

	private String generateOtp() {

		StringBuilder sb = new StringBuilder(OTP_LENGTH);
		for (int i = 0; i < OTP_LENGTH; i++) {
			sb.append(secureRandom.nextInt(10));
		}
		return sb.toString();
	}

	@Override
	public void resendOtp(SendOtpRequestDTO request) {

		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException(ApplicationConstants.USER_NOT_FOUND));

		// Generate new OTP
		String otpCode = generateOtp();

		// Create new OTP
		OtpVerification otp = OtpVerification.builder().user(user).otpCode(otpCode).purpose(DEFAULT_PURPOSE)
				.expiryTime(LocalDateTime.now().plusMinutes(OTP_VALID_MINUTES)).verified(false).attempts(0).build();

		otpVerificationRepository.save(otp);

		// Send email
		sendOtpEmail(user.getEmail(), otpCode);

		log.info("OTP resent successfully to user: {}", user.getEmail());
	}
}