package com.hrs1408.springtutorial.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

public class Token {
    @Getter
    private final String token;

    private Token(String token) {
        this.token = token;
    }

    public static Token of(Long userId, Long validityInMinutes, String secretKey) {
        var issueDate = Instant.now();
        Token token = new Token(
                Jwts.builder()
                        .claim("user_id", userId)
                        .setIssuedAt(Date.from(issueDate))
                        .setExpiration(Date.from(issueDate.plus(validityInMinutes, ChronoUnit.MINUTES)))
                        .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8)))
                        .compact()
        );
        return token;
    }

    public static Long from(String token, String secretKey) {
        return ((Claims) Jwts.parserBuilder()
                .setSigningKey(Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parse(token)
                .getBody()).get("user_id", Long.class);
    }

    public static String refresh(String refreshToken, String refreshTokenSecret) {
        var claims = Jwts.parserBuilder()
                .setSigningKey(Base64.getEncoder().encodeToString(refreshTokenSecret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(refreshToken)
                .getBody();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(1440L, ChronoUnit.MINUTES)))
                .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encodeToString(refreshTokenSecret.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }
}
