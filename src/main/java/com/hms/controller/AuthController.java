package com.hms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
import com.hms.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO<RegisterResponseDTO>> register(
            @RequestHeader(value = "Authorization") String authToken,
            @RequestHeader(value = "Accept-Language") String acceptLanguage,
            @Valid @RequestBody RegisterRequestDTO request) {

        log.info("register: {} {}", acceptLanguage, request);
        return authService.register(acceptLanguage, request);
    }

    // SEND OTP
    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponseDTO<Void>> sendOtp(
            @RequestHeader(value = "Authorization") String authToken,
            @RequestHeader(value = "Accept-Language") String acceptLanguage,
            @Valid @RequestBody SendOtpRequestDTO request) {

        log.info("sendOtp: {} {}", acceptLanguage, request);
        return authService.sendOtp(acceptLanguage, request);
    }

    // VERIFY OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponseDTO<Void>> verifyOtp(
            @RequestHeader(value = "Authorization") String authToken,
            @RequestHeader(value = "Accept-Language") String acceptLanguage,
            @Valid @RequestBody VerifyOtpRequestDTO request) {

        log.info("request: {} {}", acceptLanguage, request);
        return authService.verifyOtp(acceptLanguage, request);
    }
    
    // RESEND OTP
    @PostMapping("/resend-otp")
    public ResponseEntity<ApiResponseDTO<Void>> resendOtp(
            @RequestHeader(value = "Authorization") String authToken,
            @RequestHeader(value = "Accept-Language") String acceptLanguage,
            @Valid @RequestBody SendOtpRequestDTO request) {

        log.info("resendOtp: {} {}", acceptLanguage, request);

        return authService.resendOtp(acceptLanguage, request);
    }

    // LOGIN WITH PASSWORD
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> login(
            @RequestHeader(value = "Authorization") String authToken,
            @RequestHeader(value = "Accept-Language") String acceptLanguage,
            @Valid @RequestBody LoginRequestDTO request) {

        log.info("login: {} {}", acceptLanguage, request);
        return authService.login(acceptLanguage, request);
    }

    // LOGIN WITH OTP
    @PostMapping("/login-otp")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> loginWithOtp(
            @RequestHeader(value = "Authorization") String authToken,
            @RequestHeader(value = "Accept-Language") String acceptLanguage,
            @Valid @RequestBody LoginWithOtpRequestDTO request) {

        log.info("loginWithOtp: {}", acceptLanguage, request);
        return authService.loginWithOtp(acceptLanguage, request);
    }

    // REFRESH TOKEN
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponseDTO<RefreshTokenResponseDTO>> refreshToken(
            @RequestHeader(value = "Authorization") String authToken,
            @RequestHeader(value = "Accept-Language") String acceptLanguage,
            @Valid @RequestBody RefreshTokenRequestDTO request) {

        log.info("Refresh token endpoint called");
        return authService.refreshToken(acceptLanguage, request);
    }
}
