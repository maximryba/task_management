package com.example.task.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();
    }

    private boolean isTokenExpired(String token) {
        return getClaimsFromToken(token).getExpiration().before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = getEmailFromToken(token);
        return (email.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public String getEmailFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public boolean validateToken(String token) {
        return getClaimsFromToken(token).getExpiration().after(new Date());
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] bytes = Base64.getDecoder()
            .decode(secretKey.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(bytes, "HmacSHA256"); 
    }
    }
