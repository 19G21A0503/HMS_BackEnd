package com.hms.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.hms.dto.ApiResponseDTO;
import com.hms.dto.RoleRequestDTO;
import com.hms.dto.RoleResponseDTO;

public interface RoleService {

    ResponseEntity<ApiResponseDTO<RoleResponseDTO>> createRole(String acceptLanguage, RoleRequestDTO request);

    ResponseEntity<ApiResponseDTO<RoleResponseDTO>> getRoleById(String acceptLanguage, UUID roleId);

    ResponseEntity<ApiResponseDTO<RoleResponseDTO>> getRoleByName(String acceptLanguage, String roleName);

    ResponseEntity<ApiResponseDTO<List<RoleResponseDTO>>> getAllRoles(String acceptLanguage);

    ResponseEntity<ApiResponseDTO<RoleResponseDTO>> updateRole(String acceptLanguage, UUID roleId, RoleRequestDTO request);

    ResponseEntity<ApiResponseDTO<Void>> deleteRole(String acceptLanguage, UUID roleId);
}