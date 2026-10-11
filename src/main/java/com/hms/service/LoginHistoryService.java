package com.hms.service;

import java.util.List;
import java.util.UUID;

import com.hms.dto.LoginHistoryResponseDTO;

public interface LoginHistoryService {

    List<LoginHistoryResponseDTO> getUserLoginHistory(UUID userId);
}