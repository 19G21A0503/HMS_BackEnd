package com.hms.service;

import com.hms.dto.SendOtpRequestDTO;
import com.hms.dto.VerifyOtpRequestDTO;

public interface OtpService {

    void sendOtp(SendOtpRequestDTO request);

    void verifyOtp(VerifyOtpRequestDTO request);
    
    void resendOtp(SendOtpRequestDTO request);
}