package com.zidioproject.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.Base64;
import com.zidioproject.entity.User;

@Service
public class JwtService {

    // Use a secure 256-bit key for HS256
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final String SECRET_KEY_STRING = Base64.getEncoder().encodeToString(SECRET_KEY.getEncoded());

    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, email);
    }

    // Generate token with authorities (role) claim for role-based authorization
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        // Add role as authority with ROLE_ prefix
        claims.put("authorities", java.util.List.of(user.getRole().name()));
        return createToken(claims, user.getEmail());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        long now = System.currentTimeMillis();
        long expiration = 1000 * 60 * 60 * 10; // valid for 10 hours

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expiration))
                .signWith(SECRET_KEY)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @SuppressWarnings("unchecked")
    public java.util.List<String> extractAuthorities(String token) {
        Claims claims = extractAllClaims(token);
        return (java.util.List<String>) claims.get("authorities");
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String token, String email) {
        final String subject = extractUsername(token);
        return subject.equals(email) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}
