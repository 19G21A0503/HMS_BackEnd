package com.hms.serviceImpl;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hms.constant.ApplicationConstants;
import com.hms.dto.ApiResponseDTO;
import com.hms.dto.RoleRequestDTO;
import com.hms.dto.RoleResponseDTO;
import com.hms.entity.Role;
import com.hms.exception.DuplicateResourceException;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.RoleRepository;
import com.hms.service.RoleService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public ResponseEntity<ApiResponseDTO<RoleResponseDTO>> createRole(String acceptLanguage, RoleRequestDTO request) {
        if (roleRepository.existsByRoleName(request.getRoleName())) {
            throw new DuplicateResourceException(ApplicationConstants.ROLE_ALREADY_EXISTS);
        }

        Role role = Role.builder()
                .roleName(request.getRoleName())
                .description(request.getDescription())
                .build();

        Role saved = roleRepository.save(role);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponseDTO.<RoleResponseDTO>builder()
                        .success(true)
                        .message(ApplicationConstants.ROLE_CREATED)
                        .data(toDto(saved))
                        .build());
    }

    @Override
    public ResponseEntity<ApiResponseDTO<RoleResponseDTO>> getRoleById(String acceptLanguage, UUID roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationConstants.ROLE_NOT_FOUND));

        return ResponseEntity.ok(
                ApiResponseDTO.<RoleResponseDTO>builder()
                        .success(true)
                        .message("Role fetched successfully.")
                        .data(toDto(role))
                        .build());
    }

    @Override
    public ResponseEntity<ApiResponseDTO<RoleResponseDTO>> getRoleByName(String acceptLanguage, String roleName) {
        Role role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationConstants.ROLE_NOT_FOUND));

        return ResponseEntity.ok(
                ApiResponseDTO.<RoleResponseDTO>builder()
                        .success(true)
                        .message("Role fetched successfully.")
                        .data(toDto(role))
                        .build());
    }

    @Override
    public ResponseEntity<ApiResponseDTO<List<RoleResponseDTO>>> getAllRoles(String acceptLanguage) {
        List<RoleResponseDTO> roles = roleRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();

        return ResponseEntity.ok(
                ApiResponseDTO.<List<RoleResponseDTO>>builder()
                        .success(true)
                        .message("Roles fetched successfully.")
                        .data(roles)
                        .build());
    }

    @Override
    public ResponseEntity<ApiResponseDTO<RoleResponseDTO>> updateRole(String acceptLanguage, UUID roleId, RoleRequestDTO request) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationConstants.ROLE_NOT_FOUND));

        role.setRoleName(request.getRoleName());
        role.setDescription(request.getDescription());

        Role updated = roleRepository.save(role);

        return ResponseEntity.ok(
                ApiResponseDTO.<RoleResponseDTO>builder()
                        .success(true)
                        .message(ApplicationConstants.ROLE_UPDATED)
                        .data(toDto(updated))
                        .build());
    }

    @Override
    public ResponseEntity<ApiResponseDTO<Void>> deleteRole(String acceptLanguage, UUID roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationConstants.ROLE_NOT_FOUND));

        roleRepository.delete(role);

        return ResponseEntity.ok(
                ApiResponseDTO.<Void>builder()
                        .success(true)
                        .message(ApplicationConstants.ROLE_DELETED)
                        .build());
    }

    private RoleResponseDTO toDto(Role role) {
        return RoleResponseDTO.builder()
                .roleId(role.getRoleId())
                .roleName(role.getRoleName())
                .description(role.getDescription())
                .build();
    }
}