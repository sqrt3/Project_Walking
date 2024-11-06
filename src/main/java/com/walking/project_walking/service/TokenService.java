package com.walking.project_walking.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
//@RequiredArgsConstructor
public class TokenService {

    private final String SECRET_KEY = "your_secret_key"; // 비밀 키
    private final long EXPIRATION_TIME = 1000 * 60 * 15; // 15분
//    private final UserService userService;

    // JWT 생성
    public String createToken(String email) {
        Map<String, Object> claims = new HashMap<>();
//        Long userId = userService.getUserIdByEmail(email);
//        claims.put("userId", userId); // userId(String) 키 - userId(Long) 값 쌍
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // JWT에서 이메일 추출
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    // JWT 검증
    public boolean isTokenValid(String token, String email) {
        final String extractedEmail = extractEmail(token);
        return (extractedEmail.equals(email) && !isTokenExpired(token));
    }

    // 토큰 만료 여부 확인
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    // JWT의 Claims 추출
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
