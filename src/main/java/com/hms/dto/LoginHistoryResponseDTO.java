package com.hms.dto;

import java.time.LocalDateTime;

import com.hms.enums.LoginStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginHistoryResponseDTO {

    private LocalDateTime loginTime;

    private LocalDateTime logoutTime;

    private LoginStatus loginStatus;

    private String ipAddress;

    private String browser;

    private String operatingSystem;

    private String deviceType;

    private String sessionId;
}