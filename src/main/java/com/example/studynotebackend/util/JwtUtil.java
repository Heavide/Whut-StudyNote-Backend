package com.example.studynotebackend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long jwtExpirationMs = 24 * 60 * 60 * 1000;

    public JwtUtil(@Value("${jwt.secret}") String base64Secret) {
        byte[] decoded = Base64.getDecoder().decode(base64Secret);
        this.secretKey = Keys.hmacShaKeyFor(decoded);
    }

    public String generateToken(Long userId, String username) {
        Date now    = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /** 从 Token 里取出 username */
    public String getUsernameFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims.getSubject();
    }

    /** 从 Token 里取出 userId */
    public Long getUserIdFromToken(String token) {
        Claims claims = parseClaims(token);
        Object raw = claims.get("userId");
        if (raw instanceof Number) {
            return ((Number) raw).longValue();
        }
        return null;
    }

    /** 校验 Token 有效性 */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** 内部：解析并返回 Claims */
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
