package com.hms.service;

import org.springframework.http.ResponseEntity;

import com.hms.dto.ApiResponseDTO;
import com.hms.dto.AuthResponseDTO;
import com.hms.dto.LoginRequestDTO;
import com.hms.dto.LoginWithOtpRequestDTO;
import com.hms.dto.RefreshTokenRequestDTO;
import com.hms.dto.RefreshTokenResponseDTO;
import com.hms.dto.RegisterRequestDTO;
import com.hms.dto.RegisterResponseDTO;
import com.hms.dto.SendOtpRequestDTO;
import com.hms.dto.VerifyOtpRequestDTO;

public interface AuthService {

	ResponseEntity<ApiResponseDTO<RegisterResponseDTO>> register(String acceptLanguage, RegisterRequestDTO request);

	ResponseEntity<ApiResponseDTO<Void>> sendOtp(String acceptLanguage, SendOtpRequestDTO request);

	ResponseEntity<ApiResponseDTO<Void>> verifyOtp(String acceptLanguage, VerifyOtpRequestDTO request);

	ResponseEntity<ApiResponseDTO<AuthResponseDTO>> login(String acceptLanguage, LoginRequestDTO request);

	ResponseEntity<ApiResponseDTO<AuthResponseDTO>> loginWithOtp(String acceptLanguage, LoginWithOtpRequestDTO request);

	ResponseEntity<ApiResponseDTO<RefreshTokenResponseDTO>> refreshToken(String acceptLanguage,RefreshTokenRequestDTO request);

	ResponseEntity<ApiResponseDTO<Void>> resendOtp(String acceptLanguage, SendOtpRequestDTO request);
}