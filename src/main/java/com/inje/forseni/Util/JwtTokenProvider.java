package com.inje.forseni.Util;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
@Component
public class JwtTokenProvider {

    private final SecretKey key; // 보안 강화를 위한 SecretKey 객체
    private final long accessTokenValidity = 3600000; // 1시간 (1시간 = 3600000ms)
    private final long refreshTokenValidity = 7 * 24 * 60 * 60 * 1000; // 7일

    // ✅ Secure한 Secret Key 생성 (256비트 이상)
    private static final String SECRET_KEY = "verySecureAndLongEnoughSecretKeyForJwtToken2025!!!";

    public JwtTokenProvider() {
        // Base64 인코딩된 Secret Key
        byte[] keyBytes = Base64.getEncoder().encode(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 📌 JWT Access Token 생성
    public String createAccessToken(Long membershipId, String nick) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenValidity);

        return Jwts.builder()
                .setSubject(membershipId.toString()) // 사용자 ID 저장
                .claim("nick", nick) // 사용자 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간
                .setExpiration(validity) // 토큰 만료 시간
                .signWith(key, SignatureAlgorithm.HS256) // 서명
                .compact(); // 최종 JWT 생성
    }

    // 📌 JWT Refresh Token 생성
    public String createRefreshToken() {
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenValidity);

        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 📌 JWT 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false; // 유효하지 않은 토큰
        }
    }

    // 📌 JWT에서 사용자 ID 추출
    public String getUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
