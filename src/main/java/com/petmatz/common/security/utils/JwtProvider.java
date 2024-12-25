package com.petmatz.common.security.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 토큰 생성 및 검증을 담당하는 클래스.
 * 주어진 사용자 ID로 JWT 토큰을 생성 -> 토큰의 유효성을 검증하여 사용자 ID를 반환
 */
@Component
@Slf4j
public class JwtProvider {

    // JWT 토큰의 서명에 사용할 비밀 키
    @Value("${secret-key}")
    private String secretKey;

    /**
     * 주어진 사용자 ID와 계정 ID로 JWT 토큰을 생성하는 메서드.
     * 토큰은 1시간 동안 유효하며, 사용자 ID를 서브젝트로 설정하고 계정 ID를 클레임으로 추가.
     * @return 생성된 JWT 토큰 문자열
     */
    public String create(Long userId, String accountId) {
        // 토큰 만료 시간 설정 (1시간 후)
        Date expireDate = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));

        // 비밀 키 생성
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        // JWT 토큰 생성
        String jwt = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)  // 서명 알고리즘 및 키 설정
                .setSubject(userId.toString())            // 서브젝트에 사용자 ID 설정
                .claim("accountId", accountId)            // 계정 ID를 클레임으로 추가
                .setIssuedAt(new Date())                  // 토큰 발행 시간
                .setExpiration(expireDate)                // 토큰 만료 시간
                .compact();
        log.info("Generated JWT: {}", jwt);
        return jwt;
    }

    /**
     * 주어진 JWT 토큰의 유효성을 검증하고, 사용자 ID와 계정 ID를 반환.
     * @param jwt JWT 토큰
     * @return 유효한 경우 사용자 ID와 계정 ID를 포함한 맵 반환, 유효하지 않은 경우 null 반환
     */
    public Map<String, Object> validate(String jwt) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        try {
            var claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

            // 서브젝트에서 사용자 ID 추출
            Long userId = Long.parseLong(claims.getSubject());
            // 클레임에서 계정 ID 추출
            String accountId = claims.get("accountId", String.class);

            // 결과 맵 생성
            Map<String, Object> result = new HashMap<>();
            result.put("userId", userId);
            result.put("accountId", accountId);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return null; // 예외 발생 시 null 반환
        }
    }

    public Long validateAndGetUserId(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
            String subject = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

            return Long.parseLong(subject); // subject를 Long 타입으로 변환
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}