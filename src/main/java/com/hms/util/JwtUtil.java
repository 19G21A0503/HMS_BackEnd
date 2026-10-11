package com.hms.util;

import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private final SecretKey secretKey;

	private final long accessTokenExpiration;

	private final long refreshTokenExpiration;

	public JwtUtil(@Value("${jwt.secret}") String secret,
			@Value("${jwt.access-token-expiration}") long accessTokenExpiration,
			@Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration) {

		this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
		this.accessTokenExpiration = accessTokenExpiration;
		this.refreshTokenExpiration = refreshTokenExpiration;
	}

	public String generateAccessToken(UUID userId, String email, String role) {
		return generateToken(userId, email, role, accessTokenExpiration);
	}

	public String generateRefreshToken(UUID userId, String email) {
		return generateToken(userId, email, null, refreshTokenExpiration);
	}

	private String generateToken(UUID userId, String email, String role, long expiration) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + expiration);
		var builder = Jwts.builder().subject(email)
							.claim("userId", userId.toString())
							.issuedAt(now)
							.expiration(expiry)
							.signWith(secretKey);

		if (role != null) {
			builder.claim("role", role);
		}
		return builder.compact();
	}

	public String extractUsername(String token) {
		return extractAllClaims(token).getPayload().getSubject();
	}

	public UUID extractUserId(String token) {
		String userId = extractAllClaims(token).getPayload().get("userId", String.class);
		return UUID.fromString(userId);
	}

	public String extractRole(String token) {
		return extractAllClaims(token).getPayload().get("role", String.class);
	}

	public boolean isTokenValid(String token) {
		try {
			extractAllClaims(token);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	private Jws<Claims> extractAllClaims(String token) {
		return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
	}

	public long getAccessTokenExpiration() {
		return accessTokenExpiration;
	}

	public long getRefreshTokenExpiration() {
		return refreshTokenExpiration;
	}
}