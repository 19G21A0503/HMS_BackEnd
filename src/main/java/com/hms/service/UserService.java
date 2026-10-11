package com.hms.service;

import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.hms.dto.ApiResponseDTO;
import com.hms.dto.RegisterRequestDTO;
import com.hms.dto.RegisterResponseDTO;
import com.hms.dto.UserResponseDTO;

public interface UserService {

    RegisterResponseDTO registerUser(RegisterRequestDTO request);

    ResponseEntity<ApiResponseDTO<UserResponseDTO>> getUserById(String acceptLanguage, UUID userId);

    ResponseEntity<ApiResponseDTO<UserResponseDTO>> getUserByEmail(String acceptLanguage, String email);

    ResponseEntity<ApiResponseDTO<UserResponseDTO>> getUserByPhoneNumber(String acceptLanguage, String phoneNumber);

    ResponseEntity<ApiResponseDTO> enableUser(String acceptLanguage, UUID userId);

    ResponseEntity<ApiResponseDTO> disableUser(String acceptLanguage, UUID userId);
}