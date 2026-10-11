package com.hms.service;

import com.hms.entity.RefreshToken;
import com.hms.entity.User;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user);

    RefreshToken verifyRefreshToken(String token);

    void revokeToken(String token);

    void revokeAllUserTokens(User user);
}