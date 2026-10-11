package com.hms.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hms.dto.ApiResponseDTO;
import com.hms.dto.RoleRequestDTO;
import com.hms.dto.RoleResponseDTO;
import com.hms.service.RoleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/{version}/roles")
@RequiredArgsConstructor
@Slf4j
public class RoleController {

    private final RoleService roleService;

    // CREATE ROLE
    @PostMapping(version = "v1")
    public ResponseEntity<ApiResponseDTO<RoleResponseDTO>> createRole(
            @RequestHeader(value = "Authorization") String authToken,
            @RequestHeader(value = "Accept-Language") String acceptLanguage,
            @Valid @RequestBody RoleRequestDTO request) {

        log.info("createRole: {} {}", acceptLanguage, request);
        return roleService.createRole(acceptLanguage, request);
    }

    // GET ALL ROLES
    @GetMapping(version =  "v1")
    public ResponseEntity<ApiResponseDTO<List<RoleResponseDTO>>> getAllRoles(
            @RequestHeader(value = "Authorization") String authToken,
            @RequestHeader(value = "Accept-Language") String acceptLanguage) {

        log.info("getAllRoles: {}", acceptLanguage);
        return roleService.getAllRoles(acceptLanguage);
    }

    // GET ROLE BY ID
    @GetMapping(value = "/{roleId}", version = "v1")
    public ResponseEntity<ApiResponseDTO<RoleResponseDTO>> getRoleById(
            @RequestHeader(value = "Authorization") String authToken,
            @RequestHeader(value = "Accept-Language") String acceptLanguage,
            @PathVariable UUID roleId) {

        log.info("getRoleById: {} {}", acceptLanguage, roleId);
        return roleService.getRoleById(acceptLanguage, roleId);
    }

    // GET ROLE BY NAME
    @GetMapping(value = "/name/{roleName}", version = "v1")
    public ResponseEntity<ApiResponseDTO<RoleResponseDTO>> getRoleByName(
            @RequestHeader(value = "Authorization") String authToken,
            @RequestHeader(value = "Accept-Language") String acceptLanguage,
            @PathVariable String roleName) {

        log.info("getRoleByName: {} {}", acceptLanguage, roleName);
        return roleService.getRoleByName(acceptLanguage, roleName);
    }

    // UPDATE ROLE
    @PostMapping(value = "/{roleId}", version = "v1")
    public ResponseEntity<ApiResponseDTO<RoleResponseDTO>> updateRole(
            @RequestHeader(value = "Authorization") String authToken,
            @RequestHeader(value = "Accept-Language") String acceptLanguage,
            @PathVariable UUID roleId,
            @Valid @RequestBody RoleRequestDTO request) {

        log.info("updateRole: {} {} {}", acceptLanguage, roleId, request);
        return roleService.updateRole(acceptLanguage, roleId, request);
    }

    // DELETE ROLE
    @DeleteMapping(value = "/{roleId}", version = "v1")
    public ResponseEntity<ApiResponseDTO<Void>> deleteRole(
            @RequestHeader(value = "Authorization") String authToken,
            @RequestHeader(value = "Accept-Language") String acceptLanguage,
            @PathVariable UUID roleId) {

        log.info("deleteRole: {} {}", acceptLanguage, roleId);
        return roleService.deleteRole(acceptLanguage, roleId);
    }
}