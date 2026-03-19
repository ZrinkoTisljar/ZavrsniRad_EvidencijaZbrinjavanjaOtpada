package com.example.wasteapp.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.security.SignatureException;

@Service
public class JwtService {

    private final Key key;
    private final int expMinutes;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expMinutes:120}") int expMinutes
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expMinutes = expMinutes;
    }

    public String generateToken(Long userId, String email, String role) {
        long now = System.currentTimeMillis();
        long exp = now + (long) expMinutes * 60_000;

         return Jwts.builder()
                .setSubject(email)                // standard claim: sub
                .claim("uid", userId)           // custom claim
                .claim("role", role)            // custom claim
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(exp))
                .signWith(key)
                .compact();

    }

/** Metode: validate + extractSubject. Provjera potpisa + expiration i dohvat emaila (subject)*/
    public String extractEmail(String token) {
        return parse(token).getBody().getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            parse(token); // ako prođe parse + signature + exp -> valid
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Jws<Claims> parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

}
