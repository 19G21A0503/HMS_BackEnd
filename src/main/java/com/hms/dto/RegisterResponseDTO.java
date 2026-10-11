package com.hms.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponseDTO {

    private UUID userId;

    private String firstName;

    private String lastName;	

    private String email;

    private String phoneNumber;

    private String role;

    private Boolean emailVerified;

    private Boolean enabled;
}