package com.hms.serviceImpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
 
import com.hms.constant.ApplicationConstants;
import com.hms.dto.ApiResponseDTO;
import com.hms.dto.AuthResponseDTO;
import com.hms.dto.LoginRequestDTO;
import com.hms.dto.LoginWithOtpRequestDTO;
import com.hms.dto.RefreshTokenRequestDTO;
import com.hms.dto.RefreshTokenResponseDTO;
import com.hms.dto.RegisterRequestDTO;
import com.hms.dto.RegisterResponseDTO;
import com.hms.dto.SendOtpRequestDTO;
import com.hms.dto.UserResponseDTO;
import com.hms.dto.VerifyOtpRequestDTO;
import com.hms.entity.RefreshToken;
import com.hms.entity.User;
import com.hms.exception.InvalidCredentialsException;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.UserRepository;
import com.hms.service.AuthService;
import com.hms.service.OtpService;
import com.hms.service.RefreshTokenService;
import com.hms.service.UserService;
import com.hms.util.JwtUtil;

import lombok.AllArgsConstructor;
 
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
 
    private final UserService userService;
    private final UserRepository userRepository;
    private final OtpService otpService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
 
    @Override
    public ResponseEntity<ApiResponseDTO<RegisterResponseDTO>> register(String acceptLanguage, RegisterRequestDTO request) {
        RegisterResponseDTO response = userService.registerUser(request);
 
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponseDTO.<RegisterResponseDTO>builder()
                        .success(true)
                        .message(ApplicationConstants.USER_REGISTERED)
                        .data(response)
                        .build());
    }
 
    @Override
    public ResponseEntity<ApiResponseDTO<Void>> sendOtp(String acceptLanguage, SendOtpRequestDTO request) {
        otpService.sendOtp(request);
 
        return ResponseEntity.ok(
                ApiResponseDTO.<Void>builder()
                        .success(true)
                        .message(ApplicationConstants.OTP_SENT)
                        .build());
    }
 
    @Override
    public ResponseEntity<ApiResponseDTO<Void>> verifyOtp(String acceptLanguage, VerifyOtpRequestDTO request) {
        otpService.verifyOtp(request);
 
        return ResponseEntity.ok(
                ApiResponseDTO.<Void>builder()
                        .success(true)
                        .message(ApplicationConstants.OTP_VERIFIED)
                        .build());
    }
 
    @Override
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> login(String acceptLanguage, LoginRequestDTO request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException(ApplicationConstants.INVALID_CREDENTIALS);
        }
 
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationConstants.USER_NOT_FOUND));
 
        return ResponseEntity.ok(
                ApiResponseDTO.<AuthResponseDTO>builder()
                        .success(true)
                        .message(ApplicationConstants.LOGIN_SUCCESS)
                        .data(buildAuthResponse(user))
                        .build());
    }
 
    @Override
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> loginWithOtp(String acceptLanguage, LoginWithOtpRequestDTO request) {
        VerifyOtpRequestDTO verifyRequest = VerifyOtpRequestDTO.builder()
                .email(request.getEmail())
                .otp(request.getOtp())
                .build();
 
        otpService.verifyOtp(verifyRequest);
 
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationConstants.USER_NOT_FOUND));
 
        return ResponseEntity.ok(
                ApiResponseDTO.<AuthResponseDTO>builder()
                        .success(true)
                        .message(ApplicationConstants.LOGIN_SUCCESS)
                        .data(buildAuthResponse(user))
                        .build());
    }
 
    @Override
    public ResponseEntity<ApiResponseDTO<RefreshTokenResponseDTO>> refreshToken(String acceptLanguage, RefreshTokenRequestDTO request) {
        RefreshToken existingToken = refreshTokenService.verifyRefreshToken(request.getRefreshToken());
        User user = existingToken.getUser();
 
        // rotate: revoke the old token, issue a fresh pair
        refreshTokenService.revokeToken(existingToken.getToken());
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);
        String newAccessToken = jwtUtil.generateAccessToken(user.getUserId(), user.getEmail(), user.getRole().getRoleName());
 
        RefreshTokenResponseDTO response = RefreshTokenResponseDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken.getToken())
                .tokenType("Bearer")
                .expiresIn(jwtUtil.getAccessTokenExpiration())
                .build();
 
        return ResponseEntity.ok(
                ApiResponseDTO.<RefreshTokenResponseDTO>builder()
                        .success(true)
                        .message(ApplicationConstants.LOGIN_SUCCESS)
                        .data(response)
                        .build());
    }
 
    private AuthResponseDTO buildAuthResponse(User user) {
        String accessToken = jwtUtil.generateAccessToken(user.getUserId(), user.getEmail(), user.getRole().getRoleName());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
 
        UserResponseDTO userDto = UserResponseDTO.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole().getRoleName())
                .enabled(user.getEnabled())
                .emailVerified(user.getEmailVerified())
                .accountNonLocked(user.getAccountNonLocked())
                .accountNonExpired(user.getAccountNonExpired())
                .credentialsNonExpired(user.getCredentialsNonExpired())
                .build();
 
        return AuthResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .expiresIn(jwtUtil.getAccessTokenExpiration())
                .user(userDto)
                .build();
    }
    
    @Override
    public ResponseEntity<ApiResponseDTO<Void>> resendOtp(String acceptLanguage, SendOtpRequestDTO request) {

        otpService.resendOtp(request);

        return ResponseEntity.ok(
                ApiResponseDTO.<Void>builder()
                        .success(true)
                        .message(ApplicationConstants.OTP_RESENT)
                        .build());
    }
}