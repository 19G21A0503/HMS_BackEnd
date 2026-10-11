package com.hms.serviceImpl;

import java.util.List;
import java.util.UUID;
 
import org.springframework.stereotype.Service;
 
import com.hms.dto.LoginHistoryResponseDTO;
import com.hms.entity.LoginHistory;
import com.hms.repository.LoginHistoryRepository;
import com.hms.service.LoginHistoryService;

import lombok.AllArgsConstructor;
 
@Service
@AllArgsConstructor
public class LoginHistoryServiceImpl implements LoginHistoryService {
 
    private final LoginHistoryRepository loginHistoryRepository;
 
    @Override
    public List<LoginHistoryResponseDTO> getUserLoginHistory(UUID userId) {
        return loginHistoryRepository.findByUserUserId(userId)
                .stream()
                .map(this::toDto)
                .toList();
    }
 
    private LoginHistoryResponseDTO toDto(LoginHistory history) {
        return LoginHistoryResponseDTO.builder()
                .loginTime(history.getLoginTime())
                .logoutTime(history.getLogoutTime())
                .loginStatus(history.getLoginStatus())
                .ipAddress(history.getIpAddress())
                .browser(history.getBrowser())
                .operatingSystem(history.getOperatingSystem())
                .deviceType(history.getDeviceType())
                .sessionId(history.getSessionId())
                .build();
    }
}
