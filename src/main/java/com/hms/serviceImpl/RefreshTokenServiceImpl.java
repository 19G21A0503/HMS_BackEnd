package com.hms.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
 
import com.hms.entity.RefreshToken;
import com.hms.entity.User;
import com.hms.exception.InvalidCredentialsException;
import com.hms.repository.RefreshTokenRepository;
import com.hms.service.RefreshTokenService;
import com.hms.util.JwtUtil;

import lombok.AllArgsConstructor;
 
@Service
@AllArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
 
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
 
    @Override
    public RefreshToken createRefreshToken(User user) {
        String tokenValue = jwtUtil.generateRefreshToken(user.getUserId(), user.getEmail());
 
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(tokenValue)
                .expiryDate(LocalDateTime.now().plusSeconds(jwtUtil.getRefreshTokenExpiration() / 1000))
                .revoked(false)
                .build();
 
        return refreshTokenRepository.save(refreshToken);
    }
 
    @Override
    public RefreshToken verifyRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid refresh token"));
 
        if (Boolean.TRUE.equals(refreshToken.getRevoked())) {
            throw new InvalidCredentialsException("Refresh token has been revoked");
        }
 
        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new InvalidCredentialsException("Refresh token has expired");
        }
 
        if (!jwtUtil.isTokenValid(token)) {
            throw new InvalidCredentialsException("Invalid refresh token");
        }
 
        return refreshToken;
    }
 
    @Override
    public void revokeToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(rt -> {
            rt.setRevoked(true);
            refreshTokenRepository.save(rt);
        });
    }
 
    @Override
    public void revokeAllUserTokens(User user) {
        List<RefreshToken> tokens = refreshTokenRepository.findByUser(user);
        tokens.forEach(rt -> rt.setRevoked(true));
        refreshTokenRepository.saveAll(tokens);
    }
}
