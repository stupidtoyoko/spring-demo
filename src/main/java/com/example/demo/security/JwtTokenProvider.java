package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private final Key jwtSecret = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long accessTokenValidityMs = 1000 * 60 * 15; // 15 минут
    private final long refreshTokenValidityMs = 1000 * 60 * 60 * 24 * 7; // 7 дней

    // ------------------- Генерация access токена -------------------
    // Изменили тип с List<String> на Set<String>
    public String createAccessToken(String username, Set<String> roles) {
        // роли должны быть в списке для claims
        List<String> rolesList = new ArrayList<>(roles); // конвертируем Set -> List
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", rolesList) // вставляем роли
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidityMs))
                .signWith(jwtSecret)
                .compact();
    }

    // ------------------- Генерация refresh токена -------------------
    public String createRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidityMs))
                .signWith(jwtSecret)
                .compact();
    }

    // ------------------- Валидация токена -------------------
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // ------------------- Получение username -------------------
    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(jwtSecret).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    // ------------------- Получение ролей -------------------
    public List<SimpleGrantedAuthority> getRoles(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(jwtSecret).build()
                .parseClaimsJws(token).getBody();
        List<String> roles = claims.get("roles", List.class);
        if (roles == null) return Collections.emptyList();
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}