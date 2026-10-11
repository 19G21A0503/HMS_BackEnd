package com.hms.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hms.dto.ApiResponseDTO;
import com.hms.dto.UserResponseDTO;
import com.hms.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    // GET USER BY ID
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> getUserById(
            @RequestHeader(value = "Authorization") String authToken,
            @RequestHeader(value = "Accept-Language") String acceptLanguage,
            @PathVariable UUID userId) {

        log.info("getUserById: {} {}", acceptLanguage, userId);
        return userService.getUserById(acceptLanguage, userId);
    }

    // GET USER BY EMAIL
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> getUserByEmail(
            @RequestHeader(value = "Authorization") String authToken,
            @RequestHeader(value = "Accept-Language") String acceptLanguage,
            @PathVariable String email) {

        log.info("getUserByEmail: {} {}", acceptLanguage, email);
        return userService.getUserByEmail(acceptLanguage, email);
    }

    // GET USER BY PHONE NUMBER
    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> getUserByPhoneNumber(
            @RequestHeader(value = "Authorization") String authToken,
            @RequestHeader(value = "Accept-Language") String acceptLanguage,
            @PathVariable String phoneNumber) {

        log.info("getUserByPhoneNumber: {} {}", acceptLanguage, phoneNumber);
        return userService.getUserByPhoneNumber(acceptLanguage, phoneNumber);
    }

    // ENABLE USER
    @PatchMapping("/{userId}/enable")
    public ResponseEntity<ApiResponseDTO> enableUser(
            @RequestHeader(value = "Authorization") String authToken,
            @RequestHeader(value = "Accept-Language") String acceptLanguage,
            @PathVariable UUID userId) {

        log.info("enableUser: {} {}", acceptLanguage, userId);
        return userService.enableUser(acceptLanguage, userId);
    }

    // DISABLE USER
    @PatchMapping("/{userId}/disable")
    public ResponseEntity<ApiResponseDTO> disableUser(
            @RequestHeader(value = "Authorization") String authToken,
            @RequestHeader(value = "Accept-Language") String acceptLanguage,
            @PathVariable UUID userId) {

        log.info("disableUser: {} {}", acceptLanguage, userId);
        return userService.disableUser(acceptLanguage, userId);
    }
}