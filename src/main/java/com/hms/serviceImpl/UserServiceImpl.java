package com.hms.serviceImpl;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hms.constant.ApplicationConstants;
import com.hms.dto.ApiResponseDTO;
import com.hms.dto.RegisterRequestDTO;
import com.hms.dto.RegisterResponseDTO;
import com.hms.dto.UserResponseDTO;
import com.hms.entity.Role;
import com.hms.entity.User;
import com.hms.exception.DuplicateResourceException;
import com.hms.exception.InvalidRequestException;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.RoleRepository;
import com.hms.repository.UserRepository;
import com.hms.service.UserService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String DEFAULT_ROLE_NAME = "PATIENT";
    private static final int MIN_PHONE_DIGITS = 10;
    private static final int MAX_PHONE_DIGITS = 15;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public RegisterResponseDTO registerUser(RegisterRequestDTO request) {
        String normalizedPhone = normalizePhone(request.getPhoneNumber());

        if (normalizedPhone.length() < MIN_PHONE_DIGITS || normalizedPhone.length() > MAX_PHONE_DIGITS) {
            throw new InvalidRequestException("Phone number must contain 10 to 15 digits");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(ApplicationConstants.EMAIL_ALREADY_EXISTS);
        }
        if (userRepository.existsByPhoneNumber(normalizedPhone)) {
            throw new DuplicateResourceException(ApplicationConstants.PHONE_ALREADY_EXISTS);
        }

        Role defaultRole = roleRepository.findByRoleName(DEFAULT_ROLE_NAME)
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationConstants.ROLE_NOT_FOUND));

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(normalizedPhone)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(defaultRole)
                .build();

        User saved = userRepository.save(user);

        return RegisterResponseDTO.builder()
                .userId(saved.getUserId())
                .firstName(saved.getFirstName())
                .lastName(saved.getLastName())
                .email(saved.getEmail())
                .phoneNumber(saved.getPhoneNumber())
                .role(saved.getRole().getRoleName())
                .emailVerified(saved.getEmailVerified())
                .enabled(saved.getEnabled())
                .build();
    }

    @Override
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> getUserById(String acceptLanguage, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationConstants.USER_NOT_FOUND));

        return ResponseEntity.ok(
                ApiResponseDTO.<UserResponseDTO>builder()
                        .success(true)
                        .message("User fetched successfully.")
                        .data(toDto(user))
                        .build());
    }

    @Override
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> getUserByEmail(String acceptLanguage, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationConstants.USER_NOT_FOUND));

        return ResponseEntity.ok(
                ApiResponseDTO.<UserResponseDTO>builder()
                        .success(true)
                        .message("User fetched successfully.")
                        .data(toDto(user))
                        .build());
    }

    @Override
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> getUserByPhoneNumber(String acceptLanguage, String phoneNumber) {
        String normalizedPhone = normalizePhone(phoneNumber);
        User user = userRepository.findByPhoneNumber(normalizedPhone)
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationConstants.USER_NOT_FOUND));

        return ResponseEntity.ok(
                ApiResponseDTO.<UserResponseDTO>builder()
                        .success(true)
                        .message("User fetched successfully.")
                        .data(toDto(user))
                        .build());
    }

    @Override
    public ResponseEntity<ApiResponseDTO> enableUser(String acceptLanguage, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationConstants.USER_NOT_FOUND));
        user.setEnabled(true);
        userRepository.save(user);

        return ResponseEntity.ok(
                ApiResponseDTO.<Void>builder()
                        .success(true)
                        .message("User enabled successfully.")
                        .build());
    }

    @Override
    public ResponseEntity<ApiResponseDTO> disableUser(String acceptLanguage, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationConstants.USER_NOT_FOUND));
        user.setEnabled(false);
        userRepository.save(user);

        return ResponseEntity.ok(
                ApiResponseDTO.<Void>builder()
                        .success(true)
                        .message("User disabled successfully.")
                        .build());
    }

    private UserResponseDTO toDto(User user) {
        return UserResponseDTO.builder()
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
    }

    // Strips every character that isn't 0-9, including any exotic whitespace
    // (regular space, non-breaking space, tabs, dashes, parentheses, +).
    private String normalizePhone(String rawPhone) {
        return rawPhone == null ? "" : rawPhone.replaceAll("\\D", "");
    }
}